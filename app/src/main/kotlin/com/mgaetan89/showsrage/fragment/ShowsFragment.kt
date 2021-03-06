package com.mgaetan89.showsrage.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.ShowsPagerAdapter
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveShows
import com.mgaetan89.showsrage.extension.splitShowsAnimes
import com.mgaetan89.showsrage.model.Shows
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_shows.add_show
import kotlinx.android.synthetic.main.fragment_tabbed.swipe_refresh
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class ShowsFragment : TabbedFragment(), Callback<Shows>, View.OnClickListener, SearchView.OnQueryTextListener {
	private var splitShowsAnimes = false
	private var searchQuery: String? = null

	init {
		this.setHasOptionsMenu(true)
	}

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh?.isRefreshing = false

		error?.printStackTrace()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val activity = this.activity

		if (activity is MainActivity) {
			activity.displayHomeAsUp(false)
			activity.setTitle(R.string.shows)
		}

		this.onRefresh()
	}

	override fun onClick(view: View?) {
		if (view?.id == R.id.add_show) {
			val activity = this.activity ?: return

			activity.findViewById<TabLayout>(R.id.tabs)?.visibility = View.GONE
			activity.supportFragmentManager.beginTransaction()
					.addToBackStack("add_show")
					.replace(R.id.content, AddShowFragment.newInstance())
					.commitAllowingStateLoss()
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		this.splitShowsAnimes = this.context?.getPreferences()?.splitShowsAnimes() == true
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.shows, menu)

		val activity = this.activity
		val searchMenu = menu.findItem(R.id.menu_search)

		if (activity != null && searchMenu != null) {
			val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
			val searchView = searchMenu.actionView as SearchView
			searchView.setOnQueryTextListener(this)
			searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_shows, container, false)

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		if (item?.itemId == R.id.menu_filter) {
			ShowsFiltersFragment.newInstance(this.searchQuery)
					.show(this.childFragmentManager, "shows_filters")

			return true
		}

		return super.onOptionsItemSelected(item)
	}

	override fun onQueryTextChange(newText: String?): Boolean {
		this.searchQuery = newText

		this.logSearchEvent()
		this.sendFilterMessage()

		return true
	}

	override fun onQueryTextSubmit(query: String?): Boolean {
		this.searchQuery = query

		this.logSearchEvent()
		this.sendFilterMessage()

		return true
	}

	override fun onRefresh() {
		this.swipe_refresh?.isRefreshing = true

		SickRageApi.instance.services?.getShows(this)
	}

	override fun onStart() {
		super.onStart()

		this.updateState(!this.splitShowsAnimes)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		this.add_show?.setOnClickListener(this)
	}

	override fun success(shows: Shows?, response: Response?) {
		this.swipe_refresh?.isRefreshing = false

		val showsList = shows?.data?.values ?: return

		Realm.getDefaultInstance().use {
			it.saveShows(showsList.toList())
		}

		this.viewPager?.adapter = this.getAdapter()
	}

	override fun getAdapter() = ShowsPagerAdapter(this.childFragmentManager, this, this.splitShowsAnimes)

	override fun getTabMode() = TabLayout.MODE_FIXED

	override fun useSwipeToRefresh() = true

	internal fun sendFilterMessage() {
		val context = this.context ?: return
		val intent = Intent(Constants.Intents.ACTION_FILTER_SHOWS)
		intent.putExtra(Constants.Bundle.SEARCH_QUERY, this.searchQuery)

		LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
	}

	private fun logSearchEvent() {
		val activity = this.activity

		if (activity is MainActivity && !this.searchQuery.isNullOrBlank()) {
			val params = Bundle().apply {
				putString(FirebaseAnalytics.Param.SEARCH_TERM, searchQuery)
			}

			activity.firebaseAnalytics?.logEvent(FirebaseAnalytics.Event.SEARCH, params)
		}
	}

	companion object {
		fun newInstance() = ShowsFragment()
	}
}
