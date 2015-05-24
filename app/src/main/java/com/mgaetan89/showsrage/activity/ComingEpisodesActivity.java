package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.ComingEpisodesFragment;

public class ComingEpisodesActivity extends BaseActivity {
	@Override
	protected int getSelectedMenuItemIndex() {
		return MenuItems.COMING_EPISODES.ordinal();
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.coming_episodes;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new ComingEpisodesFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(false);
	}
}
