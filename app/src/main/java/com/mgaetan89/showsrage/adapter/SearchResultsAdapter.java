package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.databinding.AdapterSearchResultsListBinding;
import com.mgaetan89.showsrage.model.SearchResultItem;
import com.mgaetan89.showsrage.presenter.SearchResultPresenter;

import java.util.Collections;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
	@NonNull
	private List<SearchResultItem> searchResults = Collections.emptyList();

	public SearchResultsAdapter(@Nullable List<SearchResultItem> searchResults) {
		if (searchResults == null) {
			this.searchResults = Collections.emptyList();
		} else {
			this.searchResults = searchResults;
		}
	}

	@Override
	public int getItemCount() {
		return this.searchResults.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		SearchResultItem searchResult = this.searchResults.get(position);

		holder.bind(new SearchResultPresenter(searchResult));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_results_list, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private final AdapterSearchResultsListBinding binding;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.binding = DataBindingUtil.bind(view);
		}

		public void bind(SearchResultPresenter searchResult) {
			this.binding.setResult(searchResult);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();
			SearchResultItem searchResult = SearchResultsAdapter.this.searchResults.get(this.getAdapterPosition());
			int id = searchResult.getIndexerId();

			if (context != null && id != 0) {
				Intent intent = new Intent(Constants.Intents.ACTION_SEARCH_RESULT_SELECTED);
				intent.putExtra(Constants.Bundle.INDEXER_ID, id);

				LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
			}
		}
	}
}
