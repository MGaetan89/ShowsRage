package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsNetworkApiKeyFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsNetworkApiKeyFragment().getTitleResourceId()).isEqualTo(R.string.api_key);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsNetworkApiKeyFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_network_api_key);
	}
}
