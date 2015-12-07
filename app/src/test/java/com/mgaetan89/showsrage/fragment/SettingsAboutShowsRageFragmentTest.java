package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsAboutShowsRageFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsAboutShowsRageFragment().getTitleResourceId()).isEqualTo(R.string.app_name);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsAboutShowsRageFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_about_showsrage);
	}
}
