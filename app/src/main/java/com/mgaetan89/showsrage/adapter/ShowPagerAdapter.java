package com.mgaetan89.showsrage.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.SeasonFragment;
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment;

import java.util.Collections;
import java.util.List;

public class ShowPagerAdapter extends FragmentStatePagerAdapter {
	@NonNull
	private final Fragment fragment;

	@NonNull
	private List<Integer> seasons = Collections.emptyList();

	public ShowPagerAdapter(FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable List<Integer> seasons) {
		super(fragmentManager);

		this.fragment = fragment;

		if (seasons == null) {
			this.seasons = Collections.emptyList();
		} else {
			this.seasons = seasons;
		}
	}

	@Override
	public int getCount() {
		// We have at least a tab for the show overview
		return this.seasons.size() + 1;
	}

	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			ShowOverviewFragment fragment = new ShowOverviewFragment();
			fragment.setArguments(this.fragment.getArguments());

			return fragment;
		}

		Bundle arguments = new Bundle(this.fragment.getArguments());
		arguments.putInt(Constants.Bundle.INSTANCE.getSEASON_NUMBER(), this.seasons.get(position - 1));

		SeasonFragment fragment = new SeasonFragment();
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if (position == 0) {
			return this.fragment.getString(R.string.show);
		}

		Integer season = this.seasons.get(position - 1);

		if (season == 0) {
			return this.fragment.getString(R.string.specials);
		}

		return this.fragment.getString(R.string.season_number, season);
	}
}
