package com.mgaetan89.showsrage.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class HistoryFragment : Fragment(), Callback<Histories>, DialogInterface.OnClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, RealmChangeListener<RealmResults<History>> {
    private var adapter: HistoriesAdapter? = null
    private var clearHistory: FloatingActionButton? = null
    private var emptyView: TextView? = null
    private lateinit var histories: RealmResults<History>
    private lateinit var realm: Realm
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun failure(error: RetrofitError?) {
        this.swipeRefreshLayout?.isRefreshing = false

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
            this.clearHistory?.visibility = View.GONE
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.clearHistory?.visibility = View.VISIBLE
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter?.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        val id = view?.id ?: return

        if (id == R.id.clear_history) {
            this.clearHistory()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        SickRageApi.instance.services?.clearHistory(ClearHistoryCallback(this.activity))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_history, container, false)

        if (view != null) {
            this.clearHistory = view.findViewById(R.id.clear_history) as FloatingActionButton?
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?
            this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?

            this.clearHistory?.setOnClickListener(this)

            if (this.recyclerView != null) {
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)

                this.recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        swipeRefreshLayout?.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
                    }
                })
                this.recyclerView!!.layoutManager = GridLayoutManager(this.activity, columnCount)
            }

            this.swipeRefreshLayout?.setColorSchemeResources(R.color.accent)
            this.swipeRefreshLayout?.setOnRefreshListener(this)
        }

        return view
    }

    override fun onDestroyView() {
        this.clearHistory = null
        this.emptyView = null
        this.recyclerView = null
        this.swipeRefreshLayout = null

        super.onDestroyView()
    }

    override fun onRefresh() {
        this.swipeRefreshLayout?.isRefreshing = true

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
        this.adapter = HistoriesAdapter(this.context, this.histories)
        this.recyclerView?.adapter = adapter
    }

    override fun onStop() {
        if (this.histories.isValid) {
            this.histories.removeChangeListeners()
        }

        this.realm.close()

        super.onStop()
    }

    override fun success(histories: Histories?, response: Response?) {
        this.swipeRefreshLayout?.isRefreshing = false

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
