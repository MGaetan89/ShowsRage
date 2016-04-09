package com.mgaetan89.showsrage.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ShowsAdapter
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStatsWrapper
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class ShowsSectionFragment : Fragment(), View.OnClickListener {
    private var adapter: ShowsAdapter? = null
    private var emptyView: TextView? = null
    private val filteredShows = mutableListOf<Show>()
    private val receiver = FilterReceiver(this)
    private var recyclerView: RecyclerView? = null
    private val shows = mutableListOf<Show>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val arguments = this.arguments

        if (arguments != null) {
            val shows = arguments.getSerializable(Constants.Bundle.SHOWS) as Collection<Show>?

            this.filteredShows.addAll(shows ?: emptyList())
            this.shows.addAll(shows ?: emptyList())
        }

        if (!this.shows.isEmpty()) {
            val command = getCommand(this.shows)
            val parameters = getCommandParameters(this.shows)

            SickRageApi.instance.services?.getShowStats(command, parameters, ShowStatsCallback(this))
        }

        this.updateLayout()

        this.adapter?.notifyDataSetChanged()
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.add_show) {
            val fragment = AddShowFragment()
            val tabLayout = this.activity.findViewById(R.id.tabs)

            if (tabLayout != null) {
                tabLayout.visibility = View.GONE
            }

            this.activity.supportFragmentManager.beginTransaction()
                    .addToBackStack("add_show")
                    .replace(R.id.content, fragment)
                    .commit()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_shows_section, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

            val addShow = view.findViewById(R.id.add_show) as FloatingActionButton?
            addShow?.setOnClickListener(this)

            if (this.recyclerView != null) {
                val showsListLayout = PreferenceManager.getDefaultSharedPreferences(this.context).getString("display_shows_list_layout", "poster")
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)
                this.adapter = ShowsAdapter(this.filteredShows, getAdapterLayoutResource(showsListLayout))

                this.recyclerView!!.adapter = adapter
                this.recyclerView!!.layoutManager = GridLayoutManager(this.context, columnCount)
            }
        }

        return view
    }

    override fun onDestroy() {
        this.filteredShows.clear()
        this.shows.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null

        super.onDestroyView()
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this.context).unregisterReceiver(this.receiver)

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        val intentFilter = IntentFilter(Constants.Intents.ACTION_FILTER_SHOWS)

        LocalBroadcastManager.getInstance(this.context).registerReceiver(this.receiver, intentFilter)
    }

    private fun updateLayout() {
        if (this.filteredShows.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }
    }

    protected class FilterReceiver(fragment: ShowsSectionFragment) : BroadcastReceiver() {
        private val fragmentReference: WeakReference<ShowsSectionFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            val fragment = this.fragmentReference.get() ?: return
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val filterState = preferences.getString(Constants.Preferences.Fields.SHOW_FILTER_STATE, Constants.Preferences.Defaults.SHOW_FILTER_STATE)
            val filterStatus = preferences.getInt(Constants.Preferences.Fields.SHOW_FILTER_STATUS, Constants.Preferences.Defaults.SHOW_FILTER_STATUS)
            val searchQuery = intent?.getStringExtra(Constants.Bundle.SEARCH_QUERY)
            val shows = fragment.shows
            val filteredShows = shows.filter {
                match(it, ShowsFilters.State.valueOf(filterState), filterStatus, searchQuery)
            }

            fragment.filteredShows.clear()
            fragment.filteredShows.addAll(filteredShows)
            fragment.updateLayout()
            fragment.adapter?.notifyDataSetChanged()
        }

        companion object {
            protected fun match(show: Show?, filterState: ShowsFilters.State?, filterStatus: Int, searchQuery: String?): Boolean {
                return show != null &&
                        matchFilterState(show, filterState) &&
                        matchFilterStatus(show, filterStatus) &&
                        matchSearchQuery(show, searchQuery)
            }

            protected fun matchFilterState(show: Show, filterState: ShowsFilters.State?): Boolean {
                return when (filterState) {
                    ShowsFilters.State.ACTIVE -> show.paused == 0
                    ShowsFilters.State.ALL -> true
                    ShowsFilters.State.PAUSED -> show.paused == 1
                    else -> false
                }
            }

            protected fun matchFilterStatus(show: Show, filterStatus: Int): Boolean {
                if (ShowsFilters.Status.isAll(filterStatus)) {
                    return true
                }

                val showStatus = show.status?.toLowerCase()

                return when (showStatus) {
                    "continuing" -> ShowsFilters.Status.isContinuing(filterStatus)
                    "ended" -> ShowsFilters.Status.isEnded(filterStatus)
                    "unknown" -> ShowsFilters.Status.isUnknown(filterStatus)
                    else -> false
                }
            }

            protected fun matchSearchQuery(show: Show, searchQuery: String?): Boolean {
                val query = searchQuery?.trim()

                if (query.isNullOrEmpty()) {
                    return true
                }

                val showName = show.showName?.toLowerCase() ?: ""

                return showName.contains(query!!.toLowerCase())
            }
        }
    }

    private class ShowStatsCallback(fragment: ShowsSectionFragment) : Callback<ShowStatsWrapper> {
        private val fragmentReference: WeakReference<ShowsSectionFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun failure(error: RetrofitError?) {
            error?.printStackTrace()
        }

        override fun success(showStatsWrapper: ShowStatsWrapper?, response: Response?) {
            val data = showStatsWrapper?.data ?: return
            val fragment = this.fragmentReference.get() ?: return
            val filteredShows = fragment.shows
            val shows = fragment.shows
            val showStats = data.showStats

            showStats?.forEach {
                val showStatsData = it.value.data
                val indexerId = it.key

                filteredShows.forEachIndexed { i, show ->
                    if (show.indexerId == indexerId) {
                        show.episodesCount = showStatsData.total
                        show.downloaded = showStatsData.totalDone
                        show.snatched = showStatsData.totalPending

                        fragment.adapter?.notifyItemChanged(i)

                        return
                    }
                }

                shows.forEach {
                    if (it.indexerId == indexerId) {
                        it.episodesCount = showStatsData.total
                        it.downloaded = showStatsData.totalDone
                        it.snatched = showStatsData.totalPending

                        return
                    }
                }
            }

            fragment.updateLayout()
        }
    }

    companion object {
        protected fun getAdapterLayoutResource(preferredLayout: String?): Int {
            return when (preferredLayout) {
                "banner" -> R.layout.adapter_shows_list_content_banner
                else -> R.layout.adapter_shows_list_content_poster
            }
        }

        protected fun getCommand(shows: Iterable<Show>?): String {
            val command = StringBuilder()

            shows?.forEach {
                if (isShowValid(it)) {
                    if (command.length > 0) {
                        command.append("|")
                    }

                    command.append("show.stats_").append(it.indexerId)
                }
            }

            return command.toString()
        }

        protected fun getCommandParameters(shows: Iterable<Show>?): Map<String, Int> {
            val parameters = shows?.associate {
                if (isShowValid(it)) {
                    val indexerId = it.indexerId

                    "show.stats_$indexerId.indexerid" to indexerId
                } else {
                    "" to 0
                }
            } ?: emptyMap()

            return parameters.filterKeys { it.isNotEmpty() }
        }

        protected fun isShowValid(show: Show?): Boolean {
            return show != null && show.indexerId > 0
        }
    }
}
