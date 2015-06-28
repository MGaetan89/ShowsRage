package com.mgaetan89.showsrage.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter;
import com.mgaetan89.showsrage.model.SearchResult;
import com.mgaetan89.showsrage.model.SearchResultItem;
import com.mgaetan89.showsrage.model.SearchResults;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddShowFragment extends Fragment implements Callback<SearchResults>, SearchView.OnQueryTextListener {
	@Nullable
	private SearchResultsAdapter adapter = null;

	@Nullable
	private TextView emptyView = null;

	@Nullable
	private RecyclerView recyclerView = null;

	@NonNull
	private List<SearchResultItem> searchResults = new ArrayList<>();

	public AddShowFragment() {
		this.setHasOptionsMenu(true);
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_show, menu);

		FragmentActivity activity = this.getActivity();
		SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
		MenuItem searchMenu = menu.findItem(R.id.menu_search);

		SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
		searchView.setQuery(getQueryFromIntent(activity.getIntent()), true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_show, container, false);

		if (view != null) {
			this.emptyView = (TextView) view.findViewById(android.R.id.empty);
			this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);

			if (this.recyclerView != null) {
				this.adapter = new SearchResultsAdapter(this.searchResults);

				this.recyclerView.setAdapter(this.adapter);
				this.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.searchResults.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.emptyView = null;
		this.recyclerView = null;

		super.onDestroyView();
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (isQueryValid(newText)) {
			this.searchShows(newText);

			return true;
		}

		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		if (isQueryValid(query)) {
			this.searchShows(query);

			return true;
		}

		return false;
	}

	@Override
	public void success(SearchResults searchResults, Response response) {
		this.searchResults.clear();

		if (searchResults != null) {
			SearchResult searchResult = searchResults.getData();

			if (searchResult != null) {
				List<SearchResultItem> results = searchResult.getResults();

				if (results != null) {
					this.searchResults.addAll(results);
				}
			}
		}

		if (this.searchResults.isEmpty()) {
			if (this.emptyView != null) {
				this.emptyView.setVisibility(View.VISIBLE);
			}

			if (this.recyclerView != null) {
				this.recyclerView.setVisibility(View.GONE);
			}
		} else {
			if (this.emptyView != null) {
				this.emptyView.setVisibility(View.GONE);
			}

			if (this.recyclerView != null) {
				this.recyclerView.setVisibility(View.VISIBLE);
			}
		}

		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}

	/* package */
	static String getQueryFromIntent(@Nullable Intent intent) {
		if (intent == null) {
			return "";
		}

		if (!Intent.ACTION_SEARCH.equals(intent.getAction())) {
			return "";
		}

		return intent.getStringExtra(SearchManager.QUERY);
	}

	/* package */
	static boolean isQueryValid(String query) {
		if (query == null || query.isEmpty()) {
			return false;
		}

		query = query.trim();

		return !query.isEmpty();
	}

	private void searchShows(String query) {
		SickRageApi.getInstance().getServices().search(query, this);
	}
}
