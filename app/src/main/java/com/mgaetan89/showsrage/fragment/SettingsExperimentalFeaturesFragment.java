package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsExperimentalFeaturesFragment extends SettingsFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.experimental_features;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_experimental_features;
	}
}
