package com.mgaetan89.showsrage.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.fragment.ShowsSectionFragment
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowsFilters
import java.lang.ref.WeakReference

class ShowsFilterReceiver(fragment: ShowsSectionFragment) : BroadcastReceiver() {
    private val fragmentReference: WeakReference<ShowsSectionFragment>

    init {
        this.fragmentReference = WeakReference(fragment)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val fragment = this.fragmentReference.get() ?: return
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val filterState = preferences.getString(Constants.Preferences.Fields.SHOW_FILTER_STATE, Constants.Preferences.Defaults.SHOW_FILTER_STATE)
        val filterStatus = preferences.getInt(Constants.Preferences.Fields.SHOW_FILTER_STATUS, Constants.Preferences.Defaults.SHOW_FILTER_STATUS)
        val ignoreArticles = preferences.getBoolean(Constants.Preferences.Fields.IGNORE_ARTICLES, Constants.Preferences.Defaults.IGNORE_ARTICLES)
        val searchQuery = intent?.getStringExtra(Constants.Bundle.SEARCH_QUERY)
        val filteredShows = fragment.shows?.let {
            it.filter {
                match(it, ShowsFilters.State.valueOf(filterState), filterStatus, searchQuery)
            }.sortedBy {
                getSortableShowName(it, ignoreArticles)
            }
        } ?: emptyList()

        fragment.filteredShows.clear()
        fragment.filteredShows.addAll(filteredShows)
        fragment.updateLayout()
        fragment.adapter?.notifyDataSetChanged()
    }

    companion object {
        internal fun getSortableShowName(show: Show, ignoreArticles: Boolean): String? {
            return if (ignoreArticles) {
                show.showName?.replaceFirst("^(?:an?|the)\\s+".toRegex(RegexOption.IGNORE_CASE), "")
            } else {
                show.showName
            }
        }

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
