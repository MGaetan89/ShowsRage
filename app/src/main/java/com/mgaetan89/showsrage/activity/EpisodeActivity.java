package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.EpisodeFragment;

public class EpisodeActivity extends BaseActivity {
	@Override
	protected int getSelectedMenuItemIndex() {
		return MenuItems.SHOWS.ordinal();
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.episode;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new EpisodeFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(true);
	}
}
