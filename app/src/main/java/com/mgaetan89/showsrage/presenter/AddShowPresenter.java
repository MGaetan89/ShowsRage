package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter;
import com.mgaetan89.showsrage.model.SearchResultItem;

import java.util.List;

public class AddShowPresenter {
	@NonNull
	private final SearchResultsAdapter adapter;

	@NonNull
	private RecyclerView.LayoutManager layoutManager;

	@NonNull
	private final List<SearchResultItem> searchResults;

	public AddShowPresenter(@NonNull List<SearchResultItem> searchResults) {
		this.adapter = new SearchResultsAdapter(searchResults);
		this.layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
		this.searchResults = searchResults;
	}

	@NonNull
	public SearchResultsAdapter getAdapter() {
		return this.adapter;
	}

	@NonNull
	public RecyclerView.LayoutManager getLayoutManager() {
		return this.layoutManager;
	}

	public int getEmptyViewVisibility() {
		if (this.searchResults.isEmpty()) {
			return View.VISIBLE;
		}

		return View.GONE;
	}

	public int getRecyclerViewVisibility() {
		if (this.searchResults.isEmpty()) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public void notifyAdapter() {
		this.adapter.notifyDataSetChanged();
	}

	public void setContext(Context context) {
		if (context == null) {
			this.layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
		} else {
			int columnCount = context.getResources().getInteger(R.integer.shows_column_count);

			this.layoutManager = new GridLayoutManager(context, columnCount);
		}
	}
}
