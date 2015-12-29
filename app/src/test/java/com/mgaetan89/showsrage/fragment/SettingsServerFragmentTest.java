package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsServerFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsServerFragment().getTitleResourceId()).isEqualTo(R.string.server);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsServerFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_server);
	}
}
