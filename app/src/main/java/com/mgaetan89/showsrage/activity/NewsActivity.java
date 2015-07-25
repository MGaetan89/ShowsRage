package com.mgaetan89.showsrage.activity;

import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.NewsFragment;

public class NewsActivity extends BaseActivity {
	@Override
	protected boolean displayHomeAsUp() {
		return false;
	}

	@Override
	protected Fragment getFragment() {
		return new NewsFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_news;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.news;
	}
}
