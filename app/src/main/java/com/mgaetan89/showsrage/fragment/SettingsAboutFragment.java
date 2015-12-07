package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsAboutFragment extends SettingsFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.about;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_about;
	}
}
