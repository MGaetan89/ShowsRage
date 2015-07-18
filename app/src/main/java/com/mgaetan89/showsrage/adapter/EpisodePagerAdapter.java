package com.mgaetan89.showsrage.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.EpisodeDetailFragment;

import java.util.Collections;
import java.util.List;

public class EpisodePagerAdapter extends FragmentStatePagerAdapter {
	@NonNull
	private final Fragment fragment;

	@NonNull
	private List<Integer> episodes = Collections.emptyList();

	public EpisodePagerAdapter(FragmentManager fragmentManager, @NonNull Fragment fragment, @Nullable List<Integer> episodes) {
		super(fragmentManager);

		this.fragment = fragment;

		if (episodes == null) {
			this.episodes = Collections.emptyList();
		} else {
			this.episodes = episodes;
		}
	}

	@Override
	public int getCount() {
		return this.episodes.size();
	}

	@Override
	public Fragment getItem(int position) {
		Bundle arguments = new Bundle(this.fragment.getActivity().getIntent().getExtras());
		arguments.putInt(Constants.Bundle.EPISODE_NUMBER, this.episodes.get(position));

		EpisodeDetailFragment fragment = new EpisodeDetailFragment();
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Integer episode = this.episodes.get(position);

		return this.fragment.getString(R.string.episode_name_short, episode);
	}
}
