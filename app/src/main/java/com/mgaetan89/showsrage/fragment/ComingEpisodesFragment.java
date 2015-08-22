package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ComingEpisodesPagerAdapter;
import com.mgaetan89.showsrage.model.ComingEpisode;
import com.mgaetan89.showsrage.model.ComingEpisodes;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComingEpisodesFragment extends Fragment implements Callback<ComingEpisodes> {
	@Nullable
	private ComingEpisodesPagerAdapter adapter = null;

	@NonNull
	private final List<ArrayList<ComingEpisode>> comingEpisodes = new ArrayList<>();

	@NonNull
	private final List<String> sections = new ArrayList<>();

	@Nullable
	private TabLayout tabLayout = null;

	@Nullable
	private ViewPager viewPager = null;

	public ComingEpisodesFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SickRageApi.getInstance().getServices().getComingEpisodes(this);

		this.tabLayout = (TabLayout) this.getActivity().findViewById(R.id.tabs);

		if (this.viewPager != null && this.tabLayout != null) {
			this.tabLayout.setupWithViewPager(this.viewPager);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_coming_episodes, container, false);

		if (view != null) {
			this.viewPager = (ViewPager) view.findViewById(R.id.coming_episodes_pager);

			if (this.viewPager != null) {
				this.adapter = new ComingEpisodesPagerAdapter(this.getChildFragmentManager(), this.sections, this.comingEpisodes);

				this.viewPager.setAdapter(this.adapter);
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.comingEpisodes.clear();
		this.sections.clear();

		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		this.tabLayout = null;
		this.viewPager = null;

		super.onDestroyView();
	}

	@Override
	public void success(ComingEpisodes comingEpisodes, Response response) {
		if (comingEpisodes != null) {
			Map<String, ArrayList<ComingEpisode>> data = comingEpisodes.getData();

			if (data != null) {
				String statuses[] = {"missed", "today", "soon", "later"};

				for (String status : statuses) {
					if (data.containsKey(status)) {
						ArrayList<ComingEpisode> comingEpisodesForStatus = data.get(status);

						if (!comingEpisodesForStatus.isEmpty()) {
							this.comingEpisodes.add(comingEpisodesForStatus);
							this.sections.add(this.getString(getSectionName(status)));
						}
					}
				}

				if (this.adapter != null) {
					this.adapter.notifyDataSetChanged();

					if (this.tabLayout != null) {
						this.tabLayout.setTabsFromPagerAdapter(this.adapter);
						this.tabLayout.setVisibility(View.VISIBLE);
					}
				}
			}
		}
	}

	@StringRes
	/* package */ static int getSectionName(String sectionId) {
		if (sectionId != null) {
			String normalizedSectionId = sectionId.toLowerCase();

			switch (normalizedSectionId) {
				case "later":
					return R.string.later;

				case "missed":
					return R.string.missed;

				case "soon":
					return R.string.soon;

				case "today":
					return R.string.today;
			}
		}

		return 0;
	}
}
