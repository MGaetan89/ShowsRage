package com.mgaetan89.showsrage.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.SearchView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.MainActivity;
import com.mgaetan89.showsrage.adapter.ShowsPagerAdapter;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.Shows;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowsFragment extends TabbedFragment implements Callback<Shows>, SearchView.OnQueryTextListener {
	@Nullable
	private String searchQuery = null;

	@NonNull
	private final SparseArray<ArrayList<Show>> shows = new SparseArray<>();

	public ShowsFragment() {
		this.setHasOptionsMenu(true);
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity activity = this.getActivity();

		if (activity instanceof MainActivity) {
			((MainActivity) activity).displayHomeAsUp(false);
			activity.setTitle(R.string.shows);
		}

		SickRageApi.Companion.getInstance().getServices().getShows(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.shows, menu);

		FragmentActivity activity = this.getActivity();
		MenuItem searchMenu = menu.findItem(R.id.menu_search);

		if (activity != null && searchMenu != null) {
			SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);

			SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
			searchView.setOnQueryTextListener(this);
			searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
		}
	}

	@Override
	public void onDestroy() {
		this.shows.clear();

		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_filter) {
			Bundle arguments = new Bundle();
			arguments.putString(Constants.Bundle.INSTANCE.getSEARCH_QUERY(), this.searchQuery);

			ShowsFiltersFragment fragment = new ShowsFiltersFragment();
			fragment.setArguments(arguments);

			fragment.show(this.getChildFragmentManager(), "shows_filter");

			return true;
		}

		if (item.getItemId() == R.id.menu_refresh) {
			SickRageApi.Companion.getInstance().getServices().getShows(this);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		this.searchQuery = newText;

		this.sendFilterMessage();

		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		this.searchQuery = query;

		this.sendFilterMessage();

		return true;
	}

	@Override
	public void success(Shows shows, Response response) {
		Context context = this.getContext();

		if (context == null || shows == null) {
			return;
		}

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean splitShowsAnimes = preferences.getBoolean("display_split_shows_animes", false);
		Collection<Show> showsList = shows.getData().values();

		this.shows.clear();

		if (splitShowsAnimes) {
			for (Show show : showsList) {
				ArrayList<Show> showSection = this.shows.get(show.getAnime());

				if (showSection == null) {
					showSection = new ArrayList<>();

					this.shows.put(show.getAnime(), showSection);
				}

				showSection.add(show);
			}
		} else {
			ArrayList<Show> showsWrapper = new ArrayList<>();
			showsWrapper.addAll(showsList);

			this.shows.put(0, showsWrapper);
		}

		this.updateState(!splitShowsAnimes);
		this.sendFilterMessage();
	}

	@NotNull
	@Override
	protected PagerAdapter getAdapter() {
		return new ShowsPagerAdapter(this.getChildFragmentManager(), this, this.shows);
	}

	@Override
	protected int getTabMode() {
		return TabLayout.MODE_FIXED;
	}

	/* package */ void sendFilterMessage() {
		Intent intent = new Intent(Constants.Intents.INSTANCE.getACTION_FILTER_SHOWS());
		intent.putExtra(Constants.Bundle.INSTANCE.getSEARCH_QUERY(), this.searchQuery);

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
	}
}
