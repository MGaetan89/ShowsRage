package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

public class SettingsNetworkApiKeyFragment extends SettingsNetworkFragment {
	@Override
	protected int getTitleResourceId() {
		return R.string.api_key;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_network_api_key;
	}
}
