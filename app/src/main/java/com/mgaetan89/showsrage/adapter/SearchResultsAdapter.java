package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.SearchResultItem;

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

		if (holder.firstAired != null) {
			holder.firstAired.setText(DateTimeHelper.getRelativeDate(searchResult.getFirstAired(), "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS));
		}

		if (holder.indexer != null) {
			holder.indexer.setText(searchResult.getIndexerNameResource());
		}

		if (holder.name != null) {
			holder.name.setText(searchResult.getName());
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_result, parent, false);

		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Nullable
		public final TextView firstAired;

		@Nullable
		public final TextView indexer;

		@Nullable
		public final TextView name;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.firstAired = (TextView) view.findViewById(R.id.show_first_aired);
			this.indexer = (TextView) view.findViewById(R.id.show_indexer);
			this.name = (TextView) view.findViewById(R.id.show_name);
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
