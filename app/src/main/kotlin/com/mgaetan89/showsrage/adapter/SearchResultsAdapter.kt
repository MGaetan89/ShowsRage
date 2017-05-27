package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.SearchResultItem
import com.mgaetan89.showsrage.presenter.SearchResultPresenter

class SearchResultsAdapter(val searchResults: List<SearchResultItem>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {
	override fun getItemCount() = this.searchResults.size

	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		val searchResult = this.searchResults[position]

		holder?.bind(searchResult)
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_search_results_list, parent, false)

		return ViewHolder(view)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
		private val firstAired = view.findViewById(R.id.show_first_aired) as TextView
		private val indexer = view.findViewById(R.id.show_indexer) as TextView
		private val name = view.findViewById(R.id.show_name) as TextView

		init {
			this.name.isSelected = true

			view.setOnClickListener(this)
		}

		fun bind(searchResult: SearchResultItem) {
			val context = this.itemView.context
			val presenter = SearchResultPresenter(searchResult)

			this.firstAired.text = presenter.getFirstAired()
			this.indexer.text = context.getString(presenter.getIndexerNameRes())
			this.name.text = presenter.getName()
		}

		override fun onClick(view: View) {
			this.notifySearchResultSelected()
		}

		private fun notifySearchResultSelected() {
			val id = searchResults.getOrNull(this.adapterPosition)?.getIndexerId()?.takeIf { it != 0 } ?: return

			Intent(Constants.Intents.ACTION_SEARCH_RESULT_SELECTED).also {
				it.putExtra(Constants.Bundle.INDEXER_ID, id)

				LocalBroadcastManager.getInstance(this.itemView.context).sendBroadcast(it)
			}
		}
	}
}
