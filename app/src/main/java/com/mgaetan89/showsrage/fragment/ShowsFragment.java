package com.mgaetan89.showsrage.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
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

public class ShowsFragment extends Fragment implements Callback<Shows>, NavigationView.OnNavigationItemSelectedListener {
	@IntDef({FILTER_PAUSED_ACTIVE_ACTIVE, FILTER_PAUSED_ACTIVE_BOTH, FILTER_PAUSED_ACTIVE_PAUSED})
	@Retention(RetentionPolicy.SOURCE)
	private @interface FilterMode {
	}

	/* package */ static final int FILTER_PAUSED_ACTIVE_ACTIVE = 2;
	/* package */ static final int FILTER_PAUSED_ACTIVE_BOTH = 0;
	/* package */ static final int FILTER_PAUSED_ACTIVE_PAUSED = 1;

	@Nullable
	private ShowsPagerAdapter adapter = null;

	@Nullable
	private DrawerLayout filterLayout = null;

	@FilterMode
	private int filterPausedActiveMode = FILTER_PAUSED_ACTIVE_BOTH;

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

		SickRageApi.getInstance().getServices().getShows(this);

		this.tabLayout = (TabLayout) this.getActivity().findViewById(R.id.tabs);

		if (this.viewPager != null && this.tabLayout != null) {
			this.tabLayout.setTabMode(TabLayout.MODE_FIXED);
			this.tabLayout.setupWithViewPager(this.viewPager);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.shows, menu);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shows, container, false);

		if (view != null) {
			NavigationView filterContent = (NavigationView) view.findViewById(R.id.drawer_filter_content);
			this.filterLayout = (DrawerLayout) view.findViewById(R.id.drawer_filter_layout);
			this.viewPager = (ViewPager) view.findViewById(R.id.shows_pager);

			if (filterContent != null) {
				filterContent.setNavigationItemSelectedListener(this);
			}

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

		this.filterLayout = null;
		this.tabLayout = null;
		this.viewPager = null;

		super.onDestroyView();
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Filter Paused/Active shows
		if (item.getGroupId() == R.id.filter_paused_active) {
			this.filterPausedActiveMode = getFilterPausedActiveMode(item.getItemId());

			this.sendFilterMessage();

			return true;
		}

		// Filter by show status
		if (item.getGroupId() == R.id.filter_status) {
			// TODO

			return true;
		}

		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_filter) {
			if (this.filterLayout != null) {
				if (this.filterLayout.isDrawerOpen(GravityCompat.END)) {
					this.filterLayout.closeDrawer(GravityCompat.END);
				} else {
					this.filterLayout.openDrawer(GravityCompat.END);
				}
			}

			return true;
		}

		if (item.getItemId() == R.id.menu_refresh) {
			SickRageApi.getInstance().getServices().getShows(this);

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void success(Shows shows, Response response) {
		Context context = this.getContext();

		if (context == null) {
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

			if (this.tabLayout != null && this.tabLayout.getTabCount() == 0) {
				this.tabLayout.setTabsFromPagerAdapter(this.adapter);
			}
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

	private void sendFilterMessage() {
		Intent intent = new Intent(Constants.Intents.ACTION_FILTER_SHOWS);
		intent.putExtra(Constants.Bundle.FILTER_MODE, this.filterPausedActiveMode);

		LocalBroadcastManager.getInstance(this.getContext()).sendBroadcast(intent);
	}
}
