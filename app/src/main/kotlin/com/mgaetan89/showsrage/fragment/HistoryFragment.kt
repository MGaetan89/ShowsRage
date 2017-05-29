package com.mgaetan89.showsrage.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.HistoriesAdapter
import com.mgaetan89.showsrage.extension.clearHistory
import com.mgaetan89.showsrage.extension.getHistory
import com.mgaetan89.showsrage.extension.saveHistory
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.Histories
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_history.clear_history
import kotlinx.android.synthetic.main.fragment_history.empty
import kotlinx.android.synthetic.main.fragment_history.list
import kotlinx.android.synthetic.main.fragment_history.swipe_refresh
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class HistoryFragment : Fragment(), Callback<Histories>, DialogInterface.OnClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RealmChangeListener<RealmResults<History>> {
	private lateinit var histories: RealmResults<History>
	private lateinit var realm: Realm

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh.isRefreshing = false

		error?.printStackTrace()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val activity = this.activity

		if (activity is MainActivity) {
			activity.displayHomeAsUp(false)
			activity.setTitle(R.string.history)
		}
	}

	override fun onChange(histories: RealmResults<History>) {
		if (this.histories.isEmpty()) {
			this.clear_history.visibility = View.GONE
			this.empty.visibility = View.VISIBLE
			this.list.visibility = View.GONE
		} else {
			this.clear_history.visibility = View.VISIBLE
			this.empty.visibility = View.GONE
			this.list.visibility = View.VISIBLE
		}

		this.list.adapter?.notifyDataSetChanged()
	}

	override fun onClick(view: View) {
		when (view.id) {
			R.id.clear_history -> this.clearHistory()
		}
	}

	override fun onClick(dialog: DialogInterface?, which: Int) {
		SickRageApi.instance.services?.clearHistory(ClearHistoryCallback(this.activity))
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater?.inflate(R.layout.fragment_history, container, false)
	}

	override fun onRefresh() {
		this.swipe_refresh.isRefreshing = true

		SickRageApi.instance.services?.getHistory(this)
	}

	override fun onResume() {
		super.onResume()

		this.onRefresh()
	}

	override fun onStart() {
		super.onStart()

		this.realm = Realm.getDefaultInstance()
		this.histories = this.realm.getHistory(this)
		this.list.adapter = HistoriesAdapter(this.histories)
	}

	override fun onStop() {
		if (this.histories.isValid) {
			this.histories.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		this.clear_history.setOnClickListener(this)

		val columnCount = this.resources.getInteger(R.integer.shows_column_count)

		this.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)

				swipe_refresh.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
			}
		})
		this.list.layoutManager = GridLayoutManager(this.activity, columnCount)

		this.swipe_refresh.setColorSchemeResources(R.color.accent)
		this.swipe_refresh.setOnRefreshListener(this)
	}

	override fun success(histories: Histories?, response: Response?) {
		this.swipe_refresh.isRefreshing = false

		Realm.getDefaultInstance().let {
			it.saveHistory(histories?.data ?: emptyList())
			it.close()
		}
	}

	private fun clearHistory() {
		AlertDialog.Builder(this.context)
				.setMessage(R.string.clear_history_confirm)
				.setPositiveButton(R.string.clear, this)
				.setNegativeButton(android.R.string.cancel, null)
				.show()
	}

	companion object {
		fun newInstance() = HistoryFragment()
	}

	private class ClearHistoryCallback(activity: FragmentActivity) : GenericCallback(activity) {
		override fun success(genericResponse: GenericResponse?, response: Response?) {
			super.success(genericResponse, response)

			Realm.getDefaultInstance().let {
				it.clearHistory()
				it.close()
			}
		}
	}
}
