package com.mgaetan89.showsrage.activity;

import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.EpisodeFragment;

public class EpisodeActivity extends BaseActivity {
	@Override
	protected boolean displayHomeAsUp() {
		return true;
	}

	@Override
	protected Fragment getFragment() {
		return new EpisodeFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.episode;
	}
}
