package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.MainActivity;
import com.mgaetan89.showsrage.adapter.SchedulePagerAdapter;
import com.mgaetan89.showsrage.model.Schedule;
import com.mgaetan89.showsrage.model.Schedules;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ScheduleFragment extends Fragment implements Callback<Schedules> {
	@Nullable
	private SchedulePagerAdapter adapter = null;

	@NonNull
	private final List<ArrayList<Schedule>> schedule = new ArrayList<>();

	@NonNull
	private final List<String> sections = new ArrayList<>();

	@Nullable
	private TabLayout tabLayout = null;

	@Nullable
	private ViewPager viewPager = null;

	public ScheduleFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		FragmentActivity activity = this.getActivity();

		if (activity instanceof MainActivity) {
			((MainActivity) activity).displayHomeAsUp(false);
			activity.setTitle(R.string.schedule);
		}

		SickRageApi.Companion.getInstance().getServices().getSchedule(this);

		this.tabLayout = (TabLayout) this.getActivity().findViewById(R.id.tabs);

		if (this.viewPager != null && this.tabLayout != null) {
			this.tabLayout.setupWithViewPager(this.viewPager);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_schedule, container, false);

		if (view != null) {
			this.viewPager = (ViewPager) view.findViewById(R.id.schedule_pager);

			if (this.viewPager != null) {
				this.adapter = new SchedulePagerAdapter(this.getChildFragmentManager(), this.sections, this.schedule);

				this.viewPager.setAdapter(this.adapter);
			}
		}

		return view;
	}

	@Override
	public void onDestroy() {
		this.schedule.clear();
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
	public void success(Schedules schedules, Response response) {
		if (schedules != null) {
			Map<String, ArrayList<Schedule>> data = schedules.getData();

			if (data != null) {
				String statuses[] = {"missed", "today", "soon", "later"};

				for (String status : statuses) {
					if (data.containsKey(status)) {
						ArrayList<Schedule> scheduleForStatus = data.get(status);

						if (!scheduleForStatus.isEmpty()) {
							this.schedule.add(scheduleForStatus);

							if (this.isAdded()) {
								this.sections.add(this.getString(getSectionName(status)));
							} else {
								this.sections.add(status);
							}
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
