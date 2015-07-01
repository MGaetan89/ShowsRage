package com.mgaetan89.showsrage.activity;

import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter;
import com.mgaetan89.showsrage.fragment.AddShowFragment;
import com.mgaetan89.showsrage.network.SickRageApi;

public class AddShowActivity extends BaseActivity implements SearchResultsAdapter.OnSearchResultSelectedListener {
	@Override
	public void onSearchResultSelected(int indexerId) {
		SickRageApi.getInstance().getServices().addNewShow(indexerId, this);
	}

	@Override
	protected boolean displayHomeAsUp() {
		return true;
	}

	@Override
	protected Fragment getFragment() {
		return new AddShowFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.add_show;
	}
}
