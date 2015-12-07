package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsDisplayFragment extends SettingsFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.display;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_display;
	}
}
