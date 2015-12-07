package com.mgaetan89.showsrage.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.SettingsAboutFragment;
import com.mgaetan89.showsrage.fragment.SettingsBehaviorFragment;
import com.mgaetan89.showsrage.fragment.SettingsDisplayFragment;
import com.mgaetan89.showsrage.fragment.SettingsExperimentalFeaturesFragment;
import com.mgaetan89.showsrage.fragment.SettingsFragment;
import com.mgaetan89.showsrage.fragment.SettingsNetworkFragment;

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
			Uri data = this.getIntent().getData();
			String path = (data != null) ? data.getPath() : null;

			this.getFragmentManager().beginTransaction()
					.replace(R.id.content, getFragmentForPath(path))
					.commit();
		}
	}

	/* package */
	static SettingsFragment getFragmentForPath(@Nullable String path) {
		if (path != null) {
			switch (path) {
				case "/about":
					return new SettingsAboutFragment();

				case "/behavior":
					return new SettingsBehaviorFragment();

				case "/display":
					return new SettingsDisplayFragment();

				case "/experimental_features":
					return new SettingsExperimentalFeaturesFragment();

				case "/network":
					return new SettingsNetworkFragment();
			}
		}

		return new SettingsFragment();
	}
}
