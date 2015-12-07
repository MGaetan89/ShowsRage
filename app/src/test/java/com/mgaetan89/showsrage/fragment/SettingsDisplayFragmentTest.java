package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsDisplayFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsDisplayFragment().getTitleResourceId()).isEqualTo(R.string.display);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsDisplayFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_display);
	}
}
