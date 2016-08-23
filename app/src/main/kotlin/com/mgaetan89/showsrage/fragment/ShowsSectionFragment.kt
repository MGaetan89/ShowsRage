package com.mgaetan89.showsrage.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.futuremind.recyclerviewfastscroll.FastScroller
import com.futuremind.recyclerviewfastscroll.viewprovider.DefaultScrollerViewProvider
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ShowsAdapter
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getShowsFilterState
import com.mgaetan89.showsrage.extension.getShowsFilterStatus
import com.mgaetan89.showsrage.extension.getShowsListLayout
import com.mgaetan89.showsrage.extension.ignoreArticles
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStatsWrapper
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference
import java.util.*

class ShowsSectionFragment : Fragment(), RealmChangeListener<RealmResults<Show>> {
    private var adapter: ShowsAdapter? = null
    private var emptyView: TextView? = null
    private val filteredShows = mutableListOf<Show>()
    private val receiver = FilterReceiver(this)
    private var recyclerView: RecyclerView? = null
    private var shows: RealmResults<Show>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onChange(shows: RealmResults<Show>) {
        if (!(this.shows?.isEmpty() ?: true)) {
            val command = getCommand(this.shows)
            val parameters = getCommandParameters(this.shows)

            SickRageApi.instance.services?.getShowStats(command, parameters, ShowStatsCallback(this))
        }

        val intent = Intent(Constants.Intents.ACTION_FILTER_SHOWS)

        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val anime = if (this.arguments.containsKey(Constants.Bundle.ANIME)) {
            this.arguments.getBoolean(Constants.Bundle.ANIME)
        } else {
            null
        }

        this.shows = RealmManager.getShows(anime, this)
        this.swipeRefreshLayout = this.activity.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_shows_section, container, false)

        if (view != null) {
            val fastScroller = view.findViewById(R.id.fastscroll) as FastScroller?

            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

            if (this.recyclerView != null) {
                val preferences = context.getPreferences()
                val ignoreArticles = preferences.ignoreArticles()
                val showsListLayout = preferences.getShowsListLayout()
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)

                this.adapter = ShowsAdapter(this.filteredShows, showsListLayout, ignoreArticles)

                this.recyclerView!!.adapter = this.adapter
                this.recyclerView!!.layoutManager = GridLayoutManager(this.context, columnCount)

                fastScroller?.let {
                    it.setViewProvider(object : DefaultScrollerViewProvider() {
                        override fun onHandleGrabbed() {
                            super.onHandleGrabbed()

                            swipeRefreshLayout?.isEnabled = false
                        }

                        override fun onHandleReleased() {
                            super.onHandleReleased()

                            swipeRefreshLayout?.isEnabled = true
                        }
                    })

                    it.setRecyclerView(this.recyclerView)
                }
            }
        }

        return view
    }

    override fun onDestroy() {
        if (this.shows?.isValid ?: false) {
            this.shows?.removeChangeListeners()
        }

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null
        this.swipeRefreshLayout = null

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

    internal class FilterReceiver(fragment: ShowsSectionFragment) : BroadcastReceiver() {
        private val fragmentReference: WeakReference<ShowsSectionFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            val fragment = this.fragmentReference.get() ?: return
            val preferences = context?.getPreferences() ?: return
            val filterState = preferences.getShowsFilterState()
            val filterStatus = preferences.getShowsFilterStatus()
            val ignoreArticles = preferences.ignoreArticles()
            val searchQuery = intent?.getStringExtra(Constants.Bundle.SEARCH_QUERY)
            val shows = fragment.shows
            val filteredShows = if (shows == null || !shows.isLoaded) {
                emptyList()
            } else {
                shows.filter {
                    match(it, filterState, filterStatus, searchQuery)
                }.sortedWith(Comparator<Show> { first, second ->
                    val firstProperty = Utils.getSortableShowName(first, ignoreArticles)
                    val secondProperty = Utils.getSortableShowName(second, ignoreArticles)

                    firstProperty.compareTo(secondProperty)
                })
            }

            if (filteredShows != fragment.filteredShows) {
                fragment.filteredShows.clear()
                fragment.filteredShows.addAll(filteredShows)
                fragment.updateLayout()
                fragment.adapter?.notifyDataSetChanged()
            }
        }

        companion object {
            internal fun match(show: Show?, filterState: ShowsFilters.State?, filterStatus: Int, searchQuery: String?): Boolean {
                return show != null &&
                        matchFilterState(show, filterState) &&
                        matchFilterStatus(show, filterStatus) &&
                        matchSearchQuery(show, searchQuery)
            }

            internal fun matchFilterState(show: Show, filterState: ShowsFilters.State?): Boolean {
                return when (filterState) {
                    ShowsFilters.State.ACTIVE -> show.paused == 0
                    ShowsFilters.State.ALL -> true
                    ShowsFilters.State.PAUSED -> show.paused == 1
                    else -> false
                }
            }

            internal fun matchFilterStatus(show: Show, filterStatus: Int): Boolean {
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

            internal fun matchSearchQuery(show: Show, searchQuery: String?): Boolean {
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
            val filteredShows = fragment.filteredShows
            val shows = fragment.shows
            val showStats = data.showStats

            showStats?.forEach {
                val showStatsData = it.value.data
                var realmStat: RealmShowStat? = null
                val indexerId = it.key

                if (showStatsData != null) {
                    realmStat = RealmManager.saveShowStat(showStatsData, indexerId)
                }

                filteredShows.filter { it.isValid && it.indexerId == indexerId }.forEach {
                    it.stat = realmStat ?: it.stat

                    fragment.adapter?.notifyItemChanged(filteredShows.indexOf(it), Constants.Payloads.SHOWS_STATS)
                }

                if (shows?.isValid ?: false) {
                    shows!!.filter { it.isValid && it.indexerId == indexerId }.forEach {
                        it.stat = realmStat ?: it.stat
                    }
                }
            }
        }
    }

    companion object {
        internal fun getCommand(shows: Iterable<Show>?): String {
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

        internal fun getCommandParameters(shows: Iterable<Show>?): Map<String, Int> {
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

        internal fun isShowValid(show: Show?): Boolean {
            return show != null && show.indexerId > 0
        }
    }
}
