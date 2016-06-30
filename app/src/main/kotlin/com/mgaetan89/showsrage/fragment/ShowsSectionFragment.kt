package com.mgaetan89.showsrage.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.helper.ShowsFilterReceiver
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStatsWrapper
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class ShowsSectionFragment : Fragment(), RealmChangeListener<RealmResults<Show>> {
    internal var adapter: ShowsAdapter? = null
    internal val filteredShows = mutableListOf<Show>()
    internal var shows: RealmResults<Show>? = null
    private var emptyView: TextView? = null
    private val receiver = ShowsFilterReceiver(this)
    private var recyclerView: RecyclerView? = null

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
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_shows_section, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

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
        this.shows?.removeChangeListeners()

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

    internal fun updateLayout() {
        if (this.filteredShows.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
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
                }

                if (shows?.isValid ?: false) {
                    shows!!.filter { it.isValid && it.indexerId == indexerId }.forEach {
                        it.stat = realmStat ?: it.stat
                    }
                }
            }

            fragment.updateLayout()
            fragment.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        internal fun getAdapterLayoutResource(preferredLayout: String?): Int {
            return when (preferredLayout) {
                "banner" -> R.layout.adapter_shows_list_content_banner
                else -> R.layout.adapter_shows_list_content_poster
            }
        }

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
