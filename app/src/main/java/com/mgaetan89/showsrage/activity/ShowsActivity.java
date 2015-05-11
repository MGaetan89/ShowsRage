package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ShowsFragment;

public class ShowsActivity extends BaseActivity {
	@Override
	protected int getSelectedMenuItemIndex() {
		return 0;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.shows;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getSupportFragmentManager().beginTransaction()
				.replace(R.id.content, new ShowsFragment())
				.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(false);
	}
}
