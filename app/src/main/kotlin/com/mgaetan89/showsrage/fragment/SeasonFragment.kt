package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.preference.PreferenceManager
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
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.Episodes
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class SeasonFragment : Fragment(), Callback<Episodes>, SwipeRefreshLayout.OnRefreshListener {
    private var adapter: EpisodesAdapter? = null
    private var emptyView: TextView? = null
    private val episodes = mutableListOf<Episode>()
    private var recyclerView: RecyclerView? = null
    private var seasonNumber: Int = 0
    private var show: Show? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun failure(error: RetrofitError?) {
        this.swipeRefreshLayout?.isRefreshing = false

        error?.printStackTrace()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this.context)

        this.seasonNumber = this.arguments.getInt(Constants.Bundle.SEASON_NUMBER, 0)
        this.show = this.arguments.getParcelable(Constants.Bundle.SHOW_MODEL)
        this.adapter = EpisodesAdapter(this.episodes, this.seasonNumber, this.show, !preferences.getBoolean("display_episodes_sort", false))

        this.recyclerView?.adapter = this.adapter

        this.onRefresh()
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

                (this.recyclerView as RecyclerView).addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        swipeRefreshLayout?.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
                    }
                })
                this.recyclerView?.layoutManager = layoutManager
            }

            this.swipeRefreshLayout?.setColorSchemeResources(R.color.accent)
            this.swipeRefreshLayout?.setOnRefreshListener(this)
        }

        return view
    }

    override fun onDestroy() {
        this.episodes.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null
        this.swipeRefreshLayout = null

        super.onDestroyView()
    }

    override fun onRefresh() {
        this.swipeRefreshLayout?.isRefreshing = true

        SickRageApi.instance.services?.getEpisodes(this.show?.indexerId, this.seasonNumber, this)
    }

    override fun success(episodes: Episodes?, response: Response?) {
        this.swipeRefreshLayout?.isRefreshing = false

        this.episodes.clear()
        this.episodes.addAll(episodes?.data?.values ?: emptyList())

        if (this.adapter?.reversed ?: false) {
            this.episodes.reverse()
        }

        if (this.episodes.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter?.notifyDataSetChanged()
    }
}
