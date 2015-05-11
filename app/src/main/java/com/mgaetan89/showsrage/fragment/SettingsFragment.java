package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mgaetan89.showsrage.R;

public class SettingsFragment extends PreferenceFragment {
	public SettingsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.settings);
	}
}
