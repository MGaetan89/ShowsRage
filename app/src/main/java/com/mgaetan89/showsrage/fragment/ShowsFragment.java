package com.mgaetan89.showsrage.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ShowsPagerAdapter;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.model.Shows;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.Collection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowsFragment extends Fragment implements Callback<Shows> {
	@Nullable
	private ShowsPagerAdapter adapter = null;

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
}
