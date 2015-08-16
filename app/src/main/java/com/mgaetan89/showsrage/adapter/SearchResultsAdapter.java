package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.databinding.AdapterSearchResultsListBinding;
import com.mgaetan89.showsrage.model.SearchResultItem;
import com.mgaetan89.showsrage.presenter.SearchResultItemPresenter;

import java.util.Collections;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
	@NonNull
	private List<SearchResultItem> searchResults = Collections.emptyList();

	public interface OnSearchResultSelectedListener {
		void onSearchResultSelected(int indexerId);
	}

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

		holder.binding.setResult(new SearchResultItemPresenter(searchResult, holder.binding.getRoot().getContext()));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		AdapterSearchResultsListBinding binding = AdapterSearchResultsListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

		return new ViewHolder(binding);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@NonNull
		public final AdapterSearchResultsListBinding binding;

		public ViewHolder(@NonNull AdapterSearchResultsListBinding binding) {
			super(binding.getRoot());

			binding.getRoot().setOnClickListener(this);

			this.binding = binding;
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();

			if (context instanceof OnSearchResultSelectedListener) {
				SearchResultItem searchResult = SearchResultsAdapter.this.searchResults.get(this.getAdapterPosition());
				int id = searchResult.getIndexerId();

				if (id != 0) {
					((OnSearchResultSelectedListener) context).onSearchResultSelected(id);
				}
			}
		}
	}
}
