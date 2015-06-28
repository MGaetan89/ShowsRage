package com.mgaetan89.showsrage.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.SearchResultItem;
import com.mgaetan89.showsrage.network.SickRageApi;

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
			holder.indexer.setText(getIndexerName(searchResult.getIndexer()));
		}

		if (holder.logo != null) {
			holder.logo.setContentDescription(searchResult.getName());

			Indexer indexer = null;
			int indexerId = 0;

			switch (searchResult.getIndexer()) {
				case 1:
					indexer = Indexer.TVDB;
					indexerId = searchResult.getTvDbId();

					break;

				case 2:
					indexer = Indexer.TVRAGE;
					indexerId = searchResult.getTvRageId();

					break;
			}

			if (indexer != null && indexerId > 0) {
				Glide.with(holder.logo.getContext())//
						.load(SickRageApi.getInstance().getPosterUrl(indexerId, indexer))//
						.into(holder.logo);
			}
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

	@StringRes
	/* package */ static int getIndexerName(int indexerId) {
		switch (indexerId) {
			case 1:
				return R.string.the_tvdb;

			case 2:
				return R.string.tvrage;
		}

		return 0;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		@Nullable
		public TextView firstAired;

		@Nullable
		public TextView indexer;

		@Nullable
		public ImageView logo;

		@Nullable
		public TextView name;

		public ViewHolder(View view) {
			super(view);

			view.setOnClickListener(this);

			this.firstAired = (TextView) view.findViewById(R.id.show_first_aired);
			this.indexer = (TextView) view.findViewById(R.id.show_indexer);
			this.logo = (ImageView) view.findViewById(R.id.show_logo);
			this.name = (TextView) view.findViewById(R.id.show_name);
		}

		@Override
		public void onClick(View view) {
			Context context = view.getContext();

			if (context instanceof OnSearchResultSelectedListener) {
				int id = 0;
				SearchResultItem searchResult = searchResults.get(this.getAdapterPosition());

				if (searchResult.getIndexer() == 1) {
					id = searchResult.getTvDbId();
				} else if (searchResult.getIndexer() == 2) {
					id = searchResult.getTvRageId();
				}

				if (id != 0) {
					((OnSearchResultSelectedListener) context).onSearchResultSelected(id);
				}
			}
		}
	}
}
