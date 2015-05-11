package com.mgaetan89.showsrage.activity;

import android.os.Bundle;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {
	@Override
	protected int getSelectedMenuItemIndex() {
		return -1;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.settings;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getFragmentManager().beginTransaction()
					.replace(R.id.content, new SettingsFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(false);
	}
}
