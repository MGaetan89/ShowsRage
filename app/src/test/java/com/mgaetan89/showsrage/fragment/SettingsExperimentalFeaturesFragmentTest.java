package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SettingsExperimentalFeaturesFragmentTest {
	@Test
	public void getTitleResourceId() {
		assertThat(new SettingsExperimentalFeaturesFragment().getTitleResourceId()).isEqualTo(R.string.experimental_features);
	}

	@Test
	public void getXmlResourceFile() {
		assertThat(new SettingsExperimentalFeaturesFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_experimental_features);
	}
}
