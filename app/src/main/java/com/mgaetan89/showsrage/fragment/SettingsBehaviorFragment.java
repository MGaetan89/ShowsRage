package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsBehaviorFragment extends SettingsFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.behavior;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_behavior;
	}
}
