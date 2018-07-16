package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.fragment_season.empty
import kotlinx.android.synthetic.main.fragment_season.list
import kotlinx.android.synthetic.main.fragment_season.swipe_refresh
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class SeasonFragment : Fragment(), Callback<Episodes>, SwipeRefreshLayout.OnRefreshListener, RealmChangeListener<RealmResults<Episode>> {
	private lateinit var episodes: RealmResults<Episode>
	private var indexerId: Int = 0
	private lateinit var realm: Realm
	private var reversedOrder = false
	private var seasonNumber: Int = 0

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh?.isRefreshing = false

		error?.printStackTrace()
	}

	override fun onChange(episodes: RealmResults<Episode>) {
		if (this.episodes.isEmpty()) {
			this.empty?.visibility = View.VISIBLE
			this.list?.visibility = View.GONE
		} else {
			this.empty?.visibility = View.GONE
			this.list?.visibility = View.VISIBLE
		}

		this.list?.adapter?.notifyDataSetChanged()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.indexerId = this.arguments?.getInt(Constants.Bundle.INDEXER_ID) ?: 0
		this.reversedOrder = Sort.DESCENDING == this.context?.getPreferences()?.getEpisodeSort()
		this.seasonNumber = this.arguments?.getInt(Constants.Bundle.SEASON_NUMBER) ?: 0
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_season, container, false)

	override fun onRefresh() {
		this.swipe_refresh?.isRefreshing = true

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
		this.list?.adapter = EpisodesAdapter(this.episodes, this.seasonNumber, this.indexerId, this.reversedOrder)
	}

	override fun onStop() {
		if (this.episodes.isValid) {
			this.episodes.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val columnCount = this.resources.getInteger(R.integer.shows_column_count)
		val layoutManager = GridLayoutManager(this.activity, columnCount)

		this.list?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)

				swipe_refresh?.isEnabled = !(recyclerView?.canScrollVertically(-1) ?: false)
			}
		})
		this.list?.layoutManager = layoutManager
		this.list?.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))

		this.swipe_refresh?.setColorSchemeResources(R.color.accent)
		this.swipe_refresh?.setOnRefreshListener(this)
	}

	override fun success(episodes: Episodes?, response: Response?) {
		this.swipe_refresh?.isRefreshing = false

		val episodesList = episodes?.data?.map {
			it.value.number = it.key
			it.value
		} ?: emptyList()

		Realm.getDefaultInstance().use {
			it.saveEpisodes(episodesList, this.indexerId, this.seasonNumber)
		}
	}
}
