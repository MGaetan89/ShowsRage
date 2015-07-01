package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

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
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.add_show;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new AddShowFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(true);
	}
}
