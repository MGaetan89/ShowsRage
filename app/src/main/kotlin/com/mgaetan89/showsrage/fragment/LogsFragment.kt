package com.mgaetan89.showsrage.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import com.firebase.jobdispatcher.Trigger
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.LogsAdapter
import com.mgaetan89.showsrage.extension.getLogLevel
import com.mgaetan89.showsrage.extension.getLogs
import com.mgaetan89.showsrage.extension.getLogsAutoUpdateInterval
import com.mgaetan89.showsrage.extension.getLogsGroup
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveLogLevel
import com.mgaetan89.showsrage.extension.saveLogs
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.Logs
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.service.LogsAutoUpdateService
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_logs.empty
import kotlinx.android.synthetic.main.fragment_logs.list
import kotlinx.android.synthetic.main.fragment_logs.swipe_refresh
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class LogsFragment : Fragment(), Callback<Logs>, RealmChangeListener<RealmResults<LogEntry>>, SwipeRefreshLayout.OnRefreshListener {
	private var groups: Array<String>? = null
	private var jobDispatcher: FirebaseJobDispatcher? = null
	private var logLevel: LogLevel? = null
	private lateinit var logs: RealmResults<LogEntry>
	private lateinit var realm: Realm

	init {
		this.setHasOptionsMenu(true)
	}

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh?.isRefreshing = false

		error?.printStackTrace()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val activity = this.activity

		if (activity is MainActivity) {
			activity.displayHomeAsUp(false)
			activity.setTitle(R.string.logs)
		}

		this.onRefresh()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == REQUEST_CODE_FILTER) {
			if (resultCode == Activity.RESULT_OK) {
				this.groups = data?.getStringArrayExtra(Constants.Bundle.LOGS_GROUPS)

				this.list?.adapter = null

				this.getLogs(this.getLogLevel())
			}
		}
	}

	override fun onChange(logs: RealmResults<LogEntry>) {
		if (this.list?.adapter == null) {
			this.list?.adapter = LogsAdapter(this.logs)
		}

		if (this.logs.isEmpty()) {
			this.empty?.visibility = View.VISIBLE
			this.list?.visibility = View.GONE
		} else {
			this.empty?.visibility = View.GONE
			this.list?.visibility = View.VISIBLE
		}

		this.list?.adapter?.notifyDataSetChanged()
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.logs, menu)

		if (!this.realm.isClosed) {
			menu?.findItem(R.id.menu_filter)?.isVisible = this.realm.getLogsGroup().isNotEmpty()
		}

		val menuId = getMenuIdForLogLevel(this.getLogLevel())

		if (menuId > 0) {
			menu?.findItem(menuId)?.isChecked = true
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_logs, container, false)

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.groupId == R.id.menu_logs_level) {
			return this.handleLogsLevelSelection(item)
		}

		if (item?.itemId == R.id.menu_filter) {
			this.handleLogsGroupFilter()

			return true
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onRefresh() {
		this.swipe_refresh?.isRefreshing = true

		SickRageApi.instance.services?.getLogs(this.getLogLevel(), this)
	}

	override fun onStart() {
		super.onStart()

		this.realm = Realm.getDefaultInstance()
		this.logs = this.realm.getLogs(this.getLogLevel(), this.groups, this)
		this.list?.adapter = LogsAdapter(this.logs)
		this.scheduleAutoUpdate()
	}

	override fun onStop() {
		this.jobDispatcher?.cancel(AUTO_UPDATE_JOB_TAG)

		if (this.logs.isValid) {
			this.logs.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.list?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)

				swipe_refresh?.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
			}
		})
		this.list?.layoutManager = LinearLayoutManager(this.activity)

		this.swipe_refresh?.setColorSchemeResources(R.color.accent)
		this.swipe_refresh?.setOnRefreshListener(this)
	}

	override fun success(logs: Logs?, response: Response?) {
		this.swipe_refresh?.isRefreshing = false

		val logEntries = logs?.data?.map(::LogEntry) ?: emptyList()

		Realm.getDefaultInstance().let {
			it.saveLogs(this.getLogLevel(), logEntries)
			it.close()
		}

		this.activity?.invalidateOptionsMenu()
	}

	private fun getLogLevel(): LogLevel {
		this.logLevel?.let {
			return it
		}

		this.context?.let {
			return it.getPreferences().getLogLevel()
		}

		return Constants.Defaults.LOG_LEVEL
	}

	private fun getLogs(logLevel: LogLevel) {
		if (this.logs.isValid) {
			this.logs.removeAllChangeListeners()
		}

		this.logs = this.realm.getLogs(logLevel, this.groups, this)
	}

	private fun handleLogsGroupFilter() {
		LogsFilterFragment.newInstance(this.groups)
			.show(this.childFragmentManager, "logs_filter")
	}

	private fun handleLogsLevelSelection(item: MenuItem?): Boolean {
		this.logLevel = getLogLevelForMenuId(item?.itemId)

		return this.logLevel?.let {
			// Check the selected menu item
			item?.isChecked = true

			// Save the selected logs level
			this.context?.getPreferences()?.saveLogLevel(it)

			// Update the list of logs
			this.list?.adapter = null

			this.getLogs(it)

			// Refresh the list of logs
			this.onRefresh()

			true
		} ?: false
	}

	private fun scheduleAutoUpdate() {
		val autoUpdateInterval = this.context?.getPreferences().getLogsAutoUpdateInterval()

		if (autoUpdateInterval > 0) {
			this.jobDispatcher = FirebaseJobDispatcher(GooglePlayDriver(this.context))

			this.jobDispatcher?.let {
				val tolerance = autoUpdateInterval * TOLERANCE_RATIO
				val job = it.newJobBuilder()
						.setRecurring(true)
						.setService(LogsAutoUpdateService::class.java)
						.setTag(AUTO_UPDATE_JOB_TAG)
						.setTrigger(Trigger.executionWindow(autoUpdateInterval, autoUpdateInterval + tolerance.toInt()))
						.build()

				it.schedule(job)
			}
		}
	}

	companion object {
		const val REQUEST_CODE_FILTER = 1

		private const val AUTO_UPDATE_JOB_TAG = "logs-auto-update-tag"
		private const val TOLERANCE_RATIO = 0.1

		internal fun getLogLevelForMenuId(menuId: Int?) = when (menuId) {
			R.id.menu_debug -> LogLevel.DEBUG
			R.id.menu_error -> LogLevel.ERROR
			R.id.menu_info -> LogLevel.INFO
			R.id.menu_warning -> LogLevel.WARNING
			else -> null
		}

		internal fun getMenuIdForLogLevel(logLevel: LogLevel?) = when (logLevel) {
			LogLevel.DEBUG -> R.id.menu_debug
			LogLevel.ERROR -> R.id.menu_error
			LogLevel.INFO -> R.id.menu_info
			LogLevel.WARNING -> R.id.menu_warning
			else -> 0
		}

		fun newInstance() = LogsFragment()
	}
}
