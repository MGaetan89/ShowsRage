package com.mgaetan89.showsrage.activity;

import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.HistoryFragment;

public class HistoryActivity extends BaseActivity {
	@Override
	protected boolean displayHomeAsUp() {
		return false;
	}

	@Override
	protected Fragment getFragment() {
		return new HistoryFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_history;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.history;
	}
}
