package com.mgaetan89.showsrage.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.ShowsAdapter;
import com.mgaetan89.showsrage.fragment.ShowsFragment;
import com.mgaetan89.showsrage.model.Show;

public class ShowsActivity extends BaseActivity implements ShowsAdapter.OnShowSelectedListener {
	@Override
	public void onShowSelected(@NonNull Show show) {
		Intent intent = new Intent(this, ShowActivity.class);
		intent.putExtra(Constants.Bundle.SHOW_MODEL, show);

		this.startActivity(intent);
	}

	@Override
	protected boolean displayHomeAsUp() {
		return false;
	}

	@Override
	protected Fragment getFragment() {
		return new ShowsFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.shows;
	}
}
