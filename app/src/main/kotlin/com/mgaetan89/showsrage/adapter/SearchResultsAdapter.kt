package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.inflate
import com.mgaetan89.showsrage.model.SearchResultItem
import com.mgaetan89.showsrage.presenter.SearchResultPresenter
import kotlinx.android.synthetic.main.adapter_search_results_list_content.view.show_first_aired
import kotlinx.android.synthetic.main.adapter_search_results_list_content.view.show_indexer
import kotlinx.android.synthetic.main.adapter_search_results_list_content.view.show_name

class SearchResultsAdapter(val searchResults: List<SearchResultItem>) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {
	override fun getItemCount() = this.searchResults.size

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val searchResult = this.searchResults[position]

		holder.bind(searchResult)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = parent.inflate(R.layout.adapter_search_results_list)

		return ViewHolder(view)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
		private val firstAired = view.show_first_aired
		private val indexer = view.show_indexer
		private val name = view.show_name

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
