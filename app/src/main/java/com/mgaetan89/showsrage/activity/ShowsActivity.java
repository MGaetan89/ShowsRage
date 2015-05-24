package com.mgaetan89.showsrage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

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
	protected int getSelectedMenuItemIndex() {
		return MenuItems.SHOWS.ordinal();
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.shows;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new ShowsFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(false);
	}
}
