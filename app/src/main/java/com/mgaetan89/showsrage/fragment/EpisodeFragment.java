package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.mgaetan89.showsrage.adapter.EpisodePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class EpisodeFragment extends Fragment {
	@Nullable
	private EpisodePagerAdapter adapter = null;

	@NonNull
	private final List<Integer> episodes = new ArrayList<>();

	@Nullable
	private TabLayout tabLayout = null;

	public EpisodeFragment() {
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Intent intent = this.getActivity().getIntent();

		if (intent != null) {
			int episodeNumber = intent.getIntExtra(Constants.Bundle.EPISODE_NUMBER, 0);
			int episodesCount = intent.getIntExtra(Constants.Bundle.EPISODES_COUNT, 0);

			for (int i = episodesCount; i > 0; i--) {
				this.episodes.add(i);
			}

			if (this.adapter != null) {
				this.adapter.notifyDataSetChanged();

				if (this.tabLayout != null) {
					this.tabLayout.setTabsFromPagerAdapter(this.adapter);
				}
			}

			if (this.tabLayout != null) {
				this.tabLayout.getTabAt(episodesCount - episodeNumber).select();
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show, container, false);

		if (view != null) {
			this.tabLayout = (TabLayout) view.findViewById(R.id.show_tabs);
			ViewPager viewPager = (ViewPager) view.findViewById(R.id.show_pager);

			if (viewPager != null) {
				this.adapter = new EpisodePagerAdapter(this.getChildFragmentManager(), this, this.episodes);

				viewPager.setAdapter(this.adapter);

				if (this.tabLayout != null) {
					this.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
					this.tabLayout.setupWithViewPager(viewPager);
				}
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
		this.tabLayout = null;

		super.onDestroyView();
	}
}
