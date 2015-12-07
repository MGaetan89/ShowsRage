package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsBehaviorFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsBehaviorFragment().getTitleResourceId()).isEqualTo(R.string.behavior);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsBehaviorFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_behavior);
	}
}
