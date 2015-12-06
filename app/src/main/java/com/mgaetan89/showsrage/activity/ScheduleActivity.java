package com.mgaetan89.showsrage.activity;

import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ScheduleFragment;

public class ScheduleActivity extends BaseActivity {
	@Override
	protected boolean displayHomeAsUp() {
		return false;
	}

	@Override
	protected Fragment getFragment() {
		return new ScheduleFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_schedule;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.schedule;
	}
}
