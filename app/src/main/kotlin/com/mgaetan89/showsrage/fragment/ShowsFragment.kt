package com.mgaetan89.showsrage.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.MenuItemCompat
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.ShowsPagerAdapter
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.Shows
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.util.*

open class ShowsFragment : TabbedFragment(), Callback<Shows>, SearchView.OnQueryTextListener {
    private var searchQuery: String? = null
    private val shows = mutableMapOf<Int, MutableList<Show>>()

    init {
        this.setHasOptionsMenu(true)
    }

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = this.activity

        if (activity is MainActivity) {
            activity.displayHomeAsUp(false)
            activity.setTitle(R.string.shows)
        }

        SickRageApi.instance.services?.getShows(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.shows, menu)

        val activity = this.activity
        val searchMenu = menu?.findItem(R.id.menu_search)

        if (activity != null && searchMenu != null) {
            val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager?
            val searchView = MenuItemCompat.getActionView(searchMenu) as SearchView
            searchView.setOnQueryTextListener(this)
            searchView.setSearchableInfo(searchManager?.getSearchableInfo(activity.componentName))
        }
    }

    override fun onDestroyView() {
        this.shows.clear()

        super.onDestroyView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_filter) {
            val arguments = Bundle()
            arguments.putString(Constants.Bundle.SEARCH_QUERY, this.searchQuery)

            val fragment = ShowsFiltersFragment()
            fragment.arguments = arguments

            fragment.show(this.childFragmentManager, "shows_filters")

            return true
        }

        if (item?.itemId == R.id.menu_refresh) {
            SickRageApi.instance.services?.getShows(this)

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        this.searchQuery = newText

        this.sendFilterMessage()

        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        this.searchQuery = query

        this.sendFilterMessage()

        return true
    }

    override fun success(shows: Shows?, response: Response?) {
        val context = this.context ?: return

        if (shows == null) {
            return
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val splitShowsAnimes = preferences.getBoolean("display_split_shows_animes", false)
        val showsList = shows.data?.values ?: emptyList<Show>()

        this.shows.clear()

        if (splitShowsAnimes) {
            showsList.forEach {
                this.shows.getOrPut(it.anime, { mutableListOf() }).add(it)
            }
        } else {
            val showsWrapper = ArrayList<Show>()
            showsWrapper.addAll(showsList)

            this.shows.put(0, showsWrapper)
        }

        this.updateState(!splitShowsAnimes)
        this.sendFilterMessage()
    }

    override fun getAdapter(): PagerAdapter {
        return ShowsPagerAdapter(this.childFragmentManager, this, this.shows)
    }

    override fun getTabMode(): Int {
        return TabLayout.MODE_FIXED
    }

    protected fun sendFilterMessage() {
        val intent = Intent(Constants.Intents.ACTION_FILTER_SHOWS)
        intent.putExtra(Constants.Bundle.SEARCH_QUERY, this.searchQuery)

        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent)
    }
}
