package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.EpisodesAdapter
import com.mgaetan89.showsrage.extension.getEpisodeSort
import com.mgaetan89.showsrage.extension.getEpisodes
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveEpisodes
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.Episodes
import com.mgaetan89.showsrage.model.Sort
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class SeasonFragment : Fragment(), Callback<Episodes>, SwipeRefreshLayout.OnRefreshListener, RealmChangeListener<RealmResults<Episode>> {
    private lateinit var adapter: EpisodesAdapter
    private var emptyView: TextView? = null
    private lateinit var episodes: RealmResults<Episode>
    private var indexerId: Int = 0
    private lateinit var realm: Realm
    private var recyclerView: RecyclerView? = null
    private var reversedOrder = false
    private var seasonNumber: Int = 0
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun failure(error: RetrofitError?) {
        this.swipeRefreshLayout?.isRefreshing = false

        error?.printStackTrace()
    }

    override fun onChange(episodes: RealmResults<Episode>) {
        if (this.episodes.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID)
        this.reversedOrder = Sort.DESCENDING == this.context.getPreferences().getEpisodeSort()
        this.seasonNumber = this.arguments.getInt(Constants.Bundle.SEASON_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_season, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?
            this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?

            if (this.recyclerView != null) {
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)
                val layoutManager = GridLayoutManager(this.activity, columnCount)

                this.recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        swipeRefreshLayout?.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
                    }
                })
                this.recyclerView!!.layoutManager = layoutManager
            }

            this.swipeRefreshLayout?.setColorSchemeResources(R.color.accent)
            this.swipeRefreshLayout?.setOnRefreshListener(this)
        }

        return view
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null
        this.swipeRefreshLayout = null

        super.onDestroyView()
    }

    override fun onRefresh() {
        this.swipeRefreshLayout?.isRefreshing = true

        SickRageApi.instance.services?.getEpisodes(this.indexerId, this.seasonNumber, this)
    }

    override fun onResume() {
        super.onResume()

        this.onRefresh()
    }

    override fun onStart() {
        super.onStart()

        this.realm = Realm.getDefaultInstance()
        this.episodes = this.realm.getEpisodes(this.indexerId, this.seasonNumber, this.reversedOrder, this)
        this.adapter = EpisodesAdapter(this.episodes, this.seasonNumber, this.indexerId, this.reversedOrder)
        this.recyclerView?.adapter = this.adapter
    }

    override fun onStop() {
        if (this.episodes.isValid) {
            this.episodes.removeAllChangeListeners()
        }

        this.realm.close()

        super.onStop()
    }

    override fun success(episodes: Episodes?, response: Response?) {
        this.swipeRefreshLayout?.isRefreshing = false

        val episodesList = episodes?.data?.map {
            it.value.number = it.key
            it.value
        } ?: emptyList()

        Realm.getDefaultInstance().let {
            it.saveEpisodes(episodesList, this.indexerId, this.seasonNumber)
            it.close()
        }
    }
}
