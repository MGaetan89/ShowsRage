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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.futuremind.recyclerviewfastscroll.viewprovider.DefaultScrollerViewProvider
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ShowsAdapter
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.getShowsFilterState
import com.mgaetan89.showsrage.extension.getShowsFilterStatus
import com.mgaetan89.showsrage.extension.getShowsListLayout
import com.mgaetan89.showsrage.extension.ignoreArticles
import com.mgaetan89.showsrage.extension.saveShowStat
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStatsWrapper
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_shows_section.empty
import kotlinx.android.synthetic.main.fragment_shows_section.fastscroll
import kotlinx.android.synthetic.main.fragment_shows_section.list
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference
import java.util.Comparator

class ShowsSectionFragment : Fragment(), RealmChangeListener<RealmResults<Show>> {
	private val filteredShows = mutableListOf<Show>()
	private lateinit var realm: Realm
	private val receiver = FilterReceiver(this)
	private lateinit var shows: RealmResults<Show>
	private var swipeRefreshLayout: SwipeRefreshLayout? = null

	override fun onChange(shows: RealmResults<Show>) {
		if (!this.shows.isEmpty()) {
			val command = getCommand(this.shows)
			val parameters = getCommandParameters(this.shows)

			SickRageApi.instance.services?.getShowStats(command, parameters, ShowStatsCallback(this))
		}

		this.context?.let {
			val intent = Intent(Constants.Intents.ACTION_FILTER_SHOWS)

			LocalBroadcastManager.getInstance(it).sendBroadcast(intent)
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.swipeRefreshLayout = this.activity?.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_shows_section, container, false)

	override fun onDestroyView() {
		this.swipeRefreshLayout = null

		super.onDestroyView()
	}

	override fun onPause() {
		this.context?.let {
			LocalBroadcastManager.getInstance(it).unregisterReceiver(this.receiver)
		}

		super.onPause()
	}

	override fun onResume() {
		super.onResume()

		this.context?.let {
			val intentFilter = IntentFilter(Constants.Intents.ACTION_FILTER_SHOWS)

			LocalBroadcastManager.getInstance(it).registerReceiver(this.receiver, intentFilter)
		}
	}

	override fun onStart() {
		super.onStart()

		val arguments = this.arguments
		val anime = if (arguments?.containsKey(Constants.Bundle.ANIME) == true) {
			arguments.getBoolean(Constants.Bundle.ANIME)
		} else {
			null
		}

		this.realm = Realm.getDefaultInstance()
		this.shows = this.realm.getShows(anime, this)
	}

	override fun onStop() {
		if (this.shows.isValid) {
			this.shows.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val preferences = view.context.getPreferences()
		val ignoreArticles = preferences.ignoreArticles()
		val showsListLayout = preferences.getShowsListLayout()
		val columnCount = this.resources.getInteger(R.integer.shows_column_count)

		this.list?.adapter = ShowsAdapter(this.filteredShows, showsListLayout, ignoreArticles)
		this.list?.layoutManager = GridLayoutManager(this.context, columnCount)

		this.fastscroll?.setViewProvider(object : DefaultScrollerViewProvider() {
			override fun onHandleGrabbed() {
				super.onHandleGrabbed()

				swipeRefreshLayout?.isEnabled = false
			}

			override fun onHandleReleased() {
				super.onHandleReleased()

				swipeRefreshLayout?.isEnabled = true
			}
		})

		this.fastscroll?.setRecyclerView(this.list)
	}

	private fun updateLayout() {
		if (this.filteredShows.isEmpty()) {
			this.empty?.visibility = View.VISIBLE
			this.list?.visibility = View.GONE
		} else {
			this.empty?.visibility = View.GONE
			this.list?.visibility = View.VISIBLE
		}
	}

	internal class FilterReceiver(fragment: ShowsSectionFragment) : BroadcastReceiver() {
		private val fragmentReference = WeakReference(fragment)

		override fun onReceive(context: Context?, intent: Intent?) {
			val fragment = this.fragmentReference.get() ?: return
			val preferences = context?.getPreferences() ?: return
			val filterState = preferences.getShowsFilterState()
			val filterStatus = preferences.getShowsFilterStatus()
			val ignoreArticles = preferences.ignoreArticles()
			val searchQuery = intent?.getStringExtra(Constants.Bundle.SEARCH_QUERY)
			val shows = fragment.shows
			val filteredShows = if (!shows.isValid || !shows.isLoaded) {
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
			val currentFilteredShows = fragment.filteredShows.filter {
				RealmObject.isValid(it)
			}

			if (filteredShows != currentFilteredShows) {
				fragment.filteredShows.clear()
				fragment.filteredShows.addAll(filteredShows)
				fragment.list?.adapter?.notifyDataSetChanged()
			}

			fragment.updateLayout()
		}

		companion object {
			internal fun match(show: Show?, filterState: ShowsFilters.State?, filterStatus: Int, searchQuery: String?): Boolean {
				return show != null &&
						matchFilterState(show, filterState) &&
						matchFilterStatus(show, filterStatus) &&
						matchSearchQuery(show, searchQuery)
			}

			internal fun matchFilterState(show: Show, filterState: ShowsFilters.State?) = when (filterState) {
				ShowsFilters.State.ACTIVE -> show.paused == 0
				ShowsFilters.State.ALL -> true
				ShowsFilters.State.PAUSED -> show.paused == 1
				else -> false
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
		private val fragmentReference = WeakReference(fragment)

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
				val indexerId = it.key
				val showStatsData = it.value.data
				val realmStat = if (showStatsData != null) {
					Realm.getDefaultInstance().use {
						it.saveShowStat(showStatsData, indexerId)
					}
				} else {
					null
				}

				filteredShows.filter { it.isValid && it.indexerId == indexerId }.forEachIndexed { i, show ->
					show.stat = realmStat ?: show.stat

					fragment.list?.adapter?.notifyItemChanged(i, Constants.Payloads.SHOWS_STATS)
				}

				if (shows.isValid) {
					shows.filter { it.isValid && it.indexerId == indexerId }.forEach {
						it.stat = realmStat ?: it.stat
					}
				}
			}
		}
	}

	companion object {
		internal fun getCommand(shows: Iterable<Show>?): String {
			val command = StringBuilder()

			shows?.filter { isShowValid(it) }?.forEach {
				if (command.isNotEmpty()) {
					command.append("|")
				}

				command.append("show.stats_").append(it.indexerId)
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

			return parameters.filterKeys(String::isNotEmpty)
		}

		internal fun isShowValid(show: Show?) = show != null && show.indexerId > 0
	}
}
