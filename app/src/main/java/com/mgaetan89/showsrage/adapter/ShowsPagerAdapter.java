package com.mgaetan89.showsrage.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowsSectionFragment;
import com.mgaetan89.showsrage.model.Show;

import java.util.ArrayList;

public class ShowsPagerAdapter extends FragmentStatePagerAdapter {
	@NonNull
	private Fragment fragment;

	@NonNull
	private SparseArray<ArrayList<Show>> shows;

	public ShowsPagerAdapter(FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable SparseArray<ArrayList<Show>> shows) {
		super(fragmentManager);

		this.fragment = fragment;

		if (shows == null) {
			this.shows = new SparseArray<>();
		} else {
			this.shows = shows;
		}
	}

	@Override
	public int getCount() {
		return this.shows.size();
	}

	@Override
	public Fragment getItem(int position) {
		Bundle arguments = new Bundle();
		arguments.putSerializable(Constants.Bundle.INSTANCE.getSHOWS(), this.shows.get(position));

		ShowsSectionFragment fragment = new ShowsSectionFragment();
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
			case 0:
				return this.fragment.getString(R.string.shows);

			case 1:
				return this.fragment.getString(R.string.animes);
		}

		return super.getPageTitle(position);
	}
}
