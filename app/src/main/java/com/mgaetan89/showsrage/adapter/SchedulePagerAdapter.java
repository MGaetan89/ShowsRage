package com.mgaetan89.showsrage.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment;
import com.mgaetan89.showsrage.model.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchedulePagerAdapter extends FragmentStatePagerAdapter {
	@NonNull
	private List<ArrayList<Schedule>> schedules = Collections.emptyList();

	@NonNull
	private List<String> sections = Collections.emptyList();

	public SchedulePagerAdapter(FragmentManager fragmentManager, @Nullable List<String> sections, @Nullable List<ArrayList<Schedule>> schedules) {
		super(fragmentManager);

		if (schedules == null) {
			this.schedules = Collections.emptyList();
		} else {
			this.schedules = schedules;
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
		arguments.putSerializable(Constants.Bundle.SCHEDULES, this.schedules.get(position));

		ScheduleSectionFragment fragment = new ScheduleSectionFragment();
		fragment.setArguments(arguments);

		return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return this.sections.get(position);
	}
}
