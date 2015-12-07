package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsAboutShowsRageFragment extends SettingsAboutFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.app_name;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_about_showsrage;
	}
}
