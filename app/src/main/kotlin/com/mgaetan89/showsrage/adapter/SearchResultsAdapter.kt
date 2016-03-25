package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterSearchResultsListBinding
import com.mgaetan89.showsrage.model.SearchResultItem
import com.mgaetan89.showsrage.presenter.SearchResultPresenter

class SearchResultsAdapter(val searchResults: List<SearchResultItem>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {
    override fun getItemCount() = this.searchResults.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val searchResult = this.searchResults[position]

        holder?.bind(SearchResultPresenter(searchResult))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_search_results_list, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val binding: AdapterSearchResultsListBinding

        init {
            view.setOnClickListener(this)

            this.binding = DataBindingUtil.bind(view)
        }

        fun bind(searchResult: SearchResultPresenter) {
            this.binding.setResult(searchResult)
        }

        override fun onClick(view: View?) {
            val context = view?.context ?: return
            val searchResult = searchResults[this.adapterPosition]
            val id = searchResult.indexerId

            if (id != 0) {
                val intent = Intent(Constants.Intents.ACTION_SEARCH_RESULT_SELECTED)
                intent.putExtra(Constants.Bundle.INDEXER_ID, id)

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
            }
        }
    }
}
