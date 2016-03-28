package com.mgaetan89.showsrage.fragment

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter
import com.mgaetan89.showsrage.model.SearchResultItem
import com.mgaetan89.showsrage.model.SearchResults
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class AddShowFragment : Fragment(), Callback<SearchResults>, SearchView.OnQueryTextListener {
    private var adapter: SearchResultsAdapter? = null
    private var emptyView: TextView? = null
    private var recyclerView: RecyclerView? = null
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_show, menu)

        val activity = this.activity
        val searchManager = activity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchMenu = menu?.findItem(R.id.menu_search)

        val searchView = MenuItemCompat.getActionView(searchMenu) as SearchView
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(this)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
        searchView.setQuery(getQueryFromIntent(activity.intent), true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_show, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

            if (this.recyclerView != null) {
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)
                this.adapter = SearchResultsAdapter(this.searchResults)

                this.recyclerView!!.adapter = this.adapter
                this.recyclerView!!.layoutManager = GridLayoutManager(this.activity, columnCount)
            }
        }

        return view
    }

    override fun onDestroy() {
        this.searchResults.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null

        super.onDestroyView()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (isQueryValid(query)) {
            SickRageApi.instance.services?.search(query!!, this)

            return true
        }

        return false
    }

    override fun success(searchResults: SearchResults?, response: Response?) {
        this.searchResults.clear()
        this.searchResults.addAll(getSearchResults(searchResults))

        if (this.searchResults.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter?.notifyDataSetChanged()
    }

    companion object {
        fun getQueryFromIntent(intent: Intent?): String? {
            if (intent == null) {
                return ""
            }

            if (!Intent.ACTION_SEARCH.equals(intent.action)) {
                return ""
            }

            return intent.getStringExtra(SearchManager.QUERY)
        }

        fun getSearchResults(searchResults: SearchResults?): List<SearchResultItem> {
            return searchResults?.data?.results ?: emptyList()
        }

        fun isQueryValid(query: String?): Boolean {
            if (query.isNullOrEmpty()) {
                return false
            }

            return !query!!.trim().isEmpty()
        }
    }
}
