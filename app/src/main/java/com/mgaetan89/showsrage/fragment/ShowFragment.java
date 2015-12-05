package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ShowPagerAdapter;
import com.mgaetan89.showsrage.model.Seasons;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ShowFragment extends Fragment implements Callback<Seasons> {
	@Nullable
	private ShowPagerAdapter adapter = null;

	@NonNull
	private final List<Integer> seasons = new ArrayList<>();

	@Nullable
	private TabLayout tabLayout = null;

	@Nullable
	private ViewPager viewPager = null;

	public ShowFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intent = this.getActivity().getIntent();
		Show show = intent.getParcelableExtra(Constants.Bundle.SHOW_MODEL);
		String sort = getSeasonsSort(PreferenceManager.getDefaultSharedPreferences(this.getContext()));

		SickRageApi.getInstance().getServices().getSeasons(show.getIndexerId(), sort, this);

		this.tabLayout = (TabLayout) this.getActivity().findViewById(R.id.tabs);

		if (this.tabLayout != null && this.viewPager != null) {
			this.tabLayout.setupWithViewPager(this.viewPager);
			this.tabLayout.setVisibility(View.VISIBLE);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show, container, false);

		if (view != null) {
			this.viewPager = (ViewPager) view.findViewById(R.id.show_pager);

			if (this.viewPager != null) {
				this.adapter = new ShowPagerAdapter(this.getChildFragmentManager(), this, this.seasons);

				this.viewPager.setAdapter(this.adapter);
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.seasons.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.tabLayout = null;
		this.viewPager = null;

		super.onDestroyView();
	}

	@Override
	public void success(Seasons seasons, Response response) {
		this.seasons.clear();
		this.seasons.addAll(seasons.getData());

		if (this.adapter != null) {
			this.adapter.notifyDataSetChanged();

			if (this.tabLayout != null) {
				this.tabLayout.setTabsFromPagerAdapter(this.adapter);
			}
		}
	}

	/* package */
	static String getSeasonsSort(@Nullable SharedPreferences preferences) {
		if (preferences == null) {
			return "desc";
		}

		return preferences.getBoolean("display_seasons_sort", false) ? "asc" : "desc";
	}
}
