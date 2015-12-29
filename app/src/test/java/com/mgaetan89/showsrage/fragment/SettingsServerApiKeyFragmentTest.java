package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsServerApiKeyFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsServerApiKeyFragment().getTitleResourceId()).isEqualTo(R.string.api_key);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsServerApiKeyFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_server_api_key);
	}
}
