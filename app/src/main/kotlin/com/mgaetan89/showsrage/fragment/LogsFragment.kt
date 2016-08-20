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
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.LogsAdapter
import com.mgaetan89.showsrage.extension.getLogLevel
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveLogLevel
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.Logs
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class LogsFragment : Fragment(), Callback<Logs>, RealmChangeListener<RealmResults<LogEntry>>, SwipeRefreshLayout.OnRefreshListener {
    private var adapter: LogsAdapter? = null
    private var emptyView: TextView? = null
    private var groups: Array<String>? = null
    private var logs: RealmResults<LogEntry>? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    init {
        this.setHasOptionsMenu(true)
    }

    override fun failure(error: RetrofitError?) {
        this.swipeRefreshLayout?.isRefreshing = false

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

                this.adapter = null

                this.logs?.removeChangeListeners()
                this.logs = RealmManager.getLogs(this.getPreferredLogsLevel(), this.groups, this)
            }
        }
    }

    override fun onChange(logs: RealmResults<LogEntry>) {
        if (this.adapter == null && this.logs != null) {
            this.adapter = LogsAdapter(this.logs!!)

            this.recyclerView?.adapter = this.adapter
        }

        if (this.logs?.isEmpty() ?: true) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.logs = RealmManager.getLogs(this.getPreferredLogsLevel(), this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.logs, menu)

        menu?.findItem(R.id.menu_filter)?.isVisible = RealmManager.getLogsGroup().isNotEmpty()

        val menuId = getMenuIdForLogLevel(this.getPreferredLogsLevel())

        if (menuId > 0) {
            menu?.findItem(menuId)?.isChecked = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_logs, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?
            this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?

            if (this.recyclerView != null) {
                this.recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        swipeRefreshLayout?.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
                    }
                })
                this.recyclerView!!.layoutManager = LinearLayoutManager(this.activity)
            }

            this.swipeRefreshLayout?.setColorSchemeResources(R.color.accent)
            this.swipeRefreshLayout?.setOnRefreshListener(this)
        }

        return view
    }

    override fun onDestroy() {
        this.logs?.removeChangeListeners()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null
        this.swipeRefreshLayout = null

        super.onDestroyView()
    }

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
        this.swipeRefreshLayout?.isRefreshing = true

        SickRageApi.instance.services?.getLogs(this.getPreferredLogsLevel(), this)
    }

    override fun success(logs: Logs?, response: Response?) {
        this.swipeRefreshLayout?.isRefreshing = false

        val logEntries = logs?.data?.map {
            LogEntry(it)
        } ?: emptyList()

        RealmManager.saveLogs(logEntries)

        this.activity.supportInvalidateOptionsMenu()
    }

    private fun getPreferredLogsLevel(): LogLevel {
        return this.context.getPreferences().getLogLevel()
    }

    private fun handleLogsGroupFilter() {
        val arguments = Bundle()
        arguments.putStringArray(Constants.Bundle.LOGS_GROUPS, this.groups)

        val fragment = LogsFilterFragment()
        fragment.arguments = arguments
        fragment.setTargetFragment(this, REQUEST_CODE_FILTER)
        fragment.show(this.childFragmentManager, "logs_filter")
    }

    private fun handleLogsLevelSelection(item: MenuItem?): Boolean {
        val logLevel = getLogLevelForMenuId(item?.itemId)

        if (logLevel != null) {
            // Check the selected menu item
            item?.isChecked = true

            // Save the selected logs level
            this.context.getPreferences().saveLogLevel(logLevel)

            // Update the list of logs
            this.adapter = null

            this.logs?.removeChangeListeners()
            this.logs = RealmManager.getLogs(logLevel, this)

            // Refresh the list of logs
            this.onRefresh()

            return true
        }

        return false
    }

    companion object {
        private const val REQUEST_CODE_FILTER = 1

        internal fun getLogLevelForMenuId(menuId: Int?): LogLevel? {
            return when (menuId) {
                R.id.menu_debug -> LogLevel.DEBUG
                R.id.menu_error -> LogLevel.ERROR
                R.id.menu_info -> LogLevel.INFO
                R.id.menu_warning -> LogLevel.WARNING
                else -> null
            }
        }

        internal fun getMenuIdForLogLevel(logLevel: LogLevel?): Int {
            return when (logLevel) {
                LogLevel.DEBUG -> R.id.menu_debug
                LogLevel.ERROR -> R.id.menu_error
                LogLevel.INFO -> R.id.menu_info
                LogLevel.WARNING -> R.id.menu_warning
                else -> 0
            }
        }
    }
}
