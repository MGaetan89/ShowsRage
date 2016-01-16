package com.mgaetan89.showsrage.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.BaseActivity;
import com.mgaetan89.showsrage.adapter.EpisodePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EpisodeFragment extends Fragment {
	@Nullable
	private EpisodePagerAdapter adapter = null;

	@NonNull
	private final List<Integer> episodes = new ArrayList<>();

	@Nullable
	private ViewPager viewPager = null;

	public EpisodeFragment() {
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity activity = this.getActivity();

		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).displayHomeAsUp(true);
			activity.setTitle(R.string.episode);
		}

		Bundle arguments = this.getArguments();

		if (arguments != null) {
			int episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER, 0);
			int episodesCount = arguments.getInt(Constants.Bundle.EPISODES_COUNT, 0);

			for (int i = episodesCount; i > 0; i--) {
				this.episodes.add(i);
			}

			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
			boolean ascendingOrder = preferences.getBoolean("display_episodes_sort", false);

			if (ascendingOrder) {
				Collections.reverse(this.episodes);
			}

			if (this.adapter != null) {
				this.adapter.notifyDataSetChanged();

				TabLayout tabLayout = (TabLayout) activity.findViewById(R.id.tabs);

				if (tabLayout != null && this.viewPager != null) {
					tabLayout.setupWithViewPager(this.viewPager);
					tabLayout.setVisibility(View.VISIBLE);

					TabLayout.Tab tab = tabLayout.getTabAt(ascendingOrder ? episodeNumber - 1 : episodesCount - episodeNumber);

					if (tab != null) {
						tab.select();
					}
				}
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show, container, false);

		if (view != null) {
			this.viewPager = (ViewPager) view.findViewById(R.id.show_pager);

			if (this.viewPager != null) {
				this.adapter = new EpisodePagerAdapter(this.getChildFragmentManager(), this, this.episodes);

				this.viewPager.setAdapter(this.adapter);
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.episodes.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.viewPager = null;

		super.onDestroyView();
	}
}
