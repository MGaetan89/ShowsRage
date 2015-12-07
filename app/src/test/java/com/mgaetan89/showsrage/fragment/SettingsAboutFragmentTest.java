package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsAboutFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsAboutFragment().getTitleResourceId()).isEqualTo(R.string.about);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsAboutFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_about);
	}
}
