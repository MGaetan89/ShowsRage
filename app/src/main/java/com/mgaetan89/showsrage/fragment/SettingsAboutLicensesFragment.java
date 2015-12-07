package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsAboutLicensesFragment extends SettingsAboutFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.licenses;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_about_licenses;
	}
}
