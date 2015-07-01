package com.mgaetan89.showsrage.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.SettingsFragment;

public class SettingsActivity extends BaseActivity {
	@Override
	protected boolean displayHomeAsUp() {
		return true;
	}

	@Override
	protected Fragment getFragment() {
		return null;
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_settings;
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
}
