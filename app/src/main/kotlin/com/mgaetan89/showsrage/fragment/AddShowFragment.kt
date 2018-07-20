package com.mgaetan89.showsrage.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter
import com.mgaetan89.showsrage.model.SearchResultItem
import com.mgaetan89.showsrage.model.SearchResults
import com.mgaetan89.showsrage.network.SickRageApi
import kotlinx.android.synthetic.main.fragment_add_show.empty
import kotlinx.android.synthetic.main.fragment_add_show.list
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class AddShowFragment : Fragment(), Callback<SearchResults>, SearchView.OnQueryTextListener {
	private val searchResults = mutableListOf<SearchResultItem>()

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
			activity.displayHomeAsUp(true)
		}
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.add_show, menu)

		val activity = this.activity ?: return
		val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
		val searchMenu = menu.findItem(R.id.menu_search)

		(searchMenu.actionView as SearchView).also {
			it.setIconifiedByDefault(false)
			it.setOnQueryTextListener(this)
			it.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
			it.setQuery(getQueryFromIntent(activity.intent), true)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_add_show, container, false)

	override fun onDestroy() {
		this.searchResults.clear()

		super.onDestroy()
	}

	override fun onQueryTextChange(newText: String?) = false

	override fun onQueryTextSubmit(query: String?): Boolean {
		if (isQueryValid(query)) {
			if (this.list?.adapter?.itemCount == 0) {
				this.empty?.let {
					it.setText(R.string.loading)
					it.visibility = View.VISIBLE
				}
			}

			SickRageApi.instance.services?.search(query!!, this)

			return true
		}

		return false
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val columnCount = this.resources.getInteger(R.integer.shows_column_count)

		this.list?.adapter = SearchResultsAdapter(this.searchResults)
		this.list?.layoutManager = GridLayoutManager(this.activity, columnCount)
	}

	override fun success(searchResults: SearchResults?, response: Response?) {
		this.searchResults.clear()
		this.searchResults.addAll(getSearchResults(searchResults))

		if (this.searchResults.isEmpty()) {
			this.empty?.let {
				it.setText(R.string.no_results)
				it.visibility = View.VISIBLE
			}

			this.list?.visibility = View.GONE
		} else {
			this.empty?.visibility = View.GONE
			this.list?.visibility = View.VISIBLE
		}

		this.list?.adapter?.notifyDataSetChanged()
	}

	companion object {
		fun getQueryFromIntent(intent: Intent?): String?
				= if (Intent.ACTION_SEARCH != intent?.action) "" else intent.getStringExtra(SearchManager.QUERY)

		fun getSearchResults(searchResults: SearchResults?) = searchResults?.data?.results ?: emptyList()

		fun isQueryValid(query: String?) = !query.isNullOrBlank()

		fun newInstance() = AddShowFragment()
	}
}
