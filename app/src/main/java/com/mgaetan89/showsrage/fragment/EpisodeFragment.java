package com.mgaetan89.showsrage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
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
	private MaterialViewPager viewPager = null;

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
			}

			if (this.viewPager != null) {
				this.viewPager.getViewPager().setCurrentItem(episodeNumber);
			}
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show, container, false);

		if (view != null) {
			this.viewPager = (MaterialViewPager) view.findViewById(R.id.show_pager);

			if (this.viewPager != null) {
				this.adapter = new EpisodePagerAdapter(this.getChildFragmentManager(), this, this.episodes);

				this.viewPager.getToolbar().setVisibility(View.GONE);
				this.viewPager.getViewPager().setAdapter(this.adapter);
				this.viewPager.getPagerTitleStrip().setViewPager(viewPager.getViewPager());
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
