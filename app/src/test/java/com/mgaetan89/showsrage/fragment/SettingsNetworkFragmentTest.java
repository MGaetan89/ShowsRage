package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsNetworkFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsNetworkFragment().getTitleResourceId()).isEqualTo(R.string.network);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsNetworkFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_network);
	}
}
