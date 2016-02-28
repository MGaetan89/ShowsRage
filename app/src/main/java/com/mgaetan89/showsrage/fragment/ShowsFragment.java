package com.mgaetan89.showsrage.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.MainActivity;
import com.mgaetan89.showsrage.adapter.ShowsPagerAdapter;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.Shows;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowsFragment extends Fragment implements Callback<Shows>, SearchView.OnQueryTextListener {
	@IntDef({FILTER_PAUSED_ACTIVE_ACTIVE, FILTER_PAUSED_ACTIVE_BOTH, FILTER_PAUSED_ACTIVE_PAUSED})
	@Retention(RetentionPolicy.SOURCE)
	private @interface FilterMode {
	}

	@IntDef({FILTER_STATUS_ALL, FILTER_STATUS_ENDED, FILTER_STATUS_CONTINUING, FILTER_STATUS_UNKNOWN})
	@Retention(RetentionPolicy.SOURCE)
	private @interface FilterStatus {
	}

	/* package */ static final int FILTER_PAUSED_ACTIVE_ACTIVE = 0;
	/* package */ static final int FILTER_PAUSED_ACTIVE_BOTH = 1;
	/* package */ static final int FILTER_PAUSED_ACTIVE_PAUSED = 2;

	/* package */ static final int FILTER_STATUS_ALL = 0;
	/* package */ static final int FILTER_STATUS_ENDED = 1;
	/* package */ static final int FILTER_STATUS_CONTINUING = 2;
	/* package */ static final int FILTER_STATUS_UNKNOWN = 3;

	@Nullable
	private ShowsPagerAdapter adapter = null;

	@FilterMode
	private int filterPausedActiveMode = FILTER_PAUSED_ACTIVE_BOTH;

	@FilterStatus
	private int filterStatus = FILTER_STATUS_ALL;

	@Nullable
	private String searchQuery = null;

	@NonNull
	private final SparseArray<ArrayList<Show>> shows = new SparseArray<>();

	@Nullable
	private TabLayout tabLayout = null;

	@Nullable
	private ViewPager viewPager = null;

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

		this.tabLayout = (TabLayout) activity.findViewById(R.id.tabs);

		if (this.viewPager != null && this.tabLayout != null) {
			this.tabLayout.setTabMode(TabLayout.MODE_FIXED);
			this.tabLayout.setupWithViewPager(this.viewPager);
		}
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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shows, container, false);

		if (view != null) {
			this.viewPager = (ViewPager) view.findViewById(R.id.shows_pager);

			if (this.viewPager != null) {
				this.adapter = new ShowsPagerAdapter(this.getChildFragmentManager(), this, this.shows);

				this.viewPager.setAdapter(this.adapter);
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.shows.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		if (this.tabLayout != null) {
			this.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
		}

		this.tabLayout = null;
		this.viewPager = null;

		super.onDestroyView();
	}

	// TODO
	public boolean onNavigationItemSelected(MenuItem item) {
		// Filter Paused/Active shows
		/*
		if (item.getGroupId() == R.id.filter_paused_active) {
			this.filterPausedActiveMode = getFilterPausedActiveMode(item.getItemId());

			this.sendFilterMessage();

			return true;
		}

		// Filter by show status
		if (item.getGroupId() == R.id.filter_status) {
			this.filterStatus = getFilterStatus(item.getItemId());

			this.sendFilterMessage();

			return true;
		}
		*/

		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_filter) {
			new ShowsFiltersFragment().show(this.getChildFragmentManager(), "shows_filter");

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

			if (this.tabLayout != null) {
				this.tabLayout.setVisibility(View.VISIBLE);
			}
		} else {
			ArrayList<Show> showsWrapper = new ArrayList<>();
			showsWrapper.addAll(showsList);

			this.shows.put(0, showsWrapper);

			if (this.tabLayout != null) {
				this.tabLayout.setVisibility(View.GONE);
			}
		}

		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();
		}
	}

	@FilterMode
	/* package */ static int getFilterPausedActiveMode(@IdRes int filterId) {
		switch (filterId) {
			case R.id.filter_active:
				return FILTER_PAUSED_ACTIVE_ACTIVE;

			case R.id.filter_all:
				return FILTER_PAUSED_ACTIVE_BOTH;

			case R.id.filter_paused:
				return FILTER_PAUSED_ACTIVE_PAUSED;
		}

		return FILTER_PAUSED_ACTIVE_BOTH;
	}

	@FilterStatus
	/* package */ static int getFilterStatus(@IdRes int filterId) {
		switch (filterId) {
			case R.id.filter_status_all:
				return FILTER_STATUS_ALL;

			case R.id.filter_status_continuing:
				return FILTER_STATUS_CONTINUING;

			case R.id.filter_status_ended:
				return FILTER_STATUS_ENDED;

			case R.id.filter_status_unknown:
				return FILTER_STATUS_UNKNOWN;
		}

		return FILTER_STATUS_ALL;
	}

	/* package */ void sendFilterMessage() {
		Intent intent = new Intent(Constants.Intents.INSTANCE.getACTION_FILTER_SHOWS());
		intent.putExtra(Constants.Bundle.INSTANCE.getFILTER_MODE(), this.filterPausedActiveMode);
		intent.putExtra(Constants.Bundle.INSTANCE.getFILTER_STATUS(), this.filterStatus);
		intent.putExtra(Constants.Bundle.INSTANCE.getSEARCH_QUERY(), this.searchQuery);

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
	}
}
