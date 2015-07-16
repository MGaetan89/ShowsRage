package com.mgaetan89.showsrage.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.fragment.ComingEpisodesSectionFragment;
import com.mgaetan89.showsrage.model.ComingEpisode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComingEpisodesPagerAdapter extends FragmentStatePagerAdapter {
	@NonNull
	private List<ArrayList<ComingEpisode>> comingEpisodes = Collections.emptyList();

	@NonNull
	private List<String> sections = Collections.emptyList();

	public ComingEpisodesPagerAdapter(FragmentManager fragmentManager, @Nullable List<String> sections, @Nullable List<ArrayList<ComingEpisode>> comingEpisodes) {
		super(fragmentManager);

		if (comingEpisodes == null) {
			this.comingEpisodes = Collections.emptyList();
		} else {
			this.comingEpisodes = comingEpisodes;
		}

		if (sections == null) {
			this.sections = Collections.emptyList();
		} else {
			this.sections = sections;
		}
	}

	@Override
	public int getCount() {
		return this.sections.size();
	}

	@Override
	public Fragment getItem(int position) {
		Bundle arguments = new Bundle();
		arguments.putSerializable(Constants.Bundle.COMING_EPISODES, this.comingEpisodes.get(position));

		ComingEpisodesSectionFragment fragment = new ComingEpisodesSectionFragment();
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return this.sections.get(position);
	}
}
