package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsFragment().getTitleResourceId()).isEqualTo(R.string.settings);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsFragment().getXmlResourceFile()).isEqualTo(R.xml.settings);
	}
}
