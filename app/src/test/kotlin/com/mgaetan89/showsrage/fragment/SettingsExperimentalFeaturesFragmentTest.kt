package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsExperimentalFeaturesFragmentTest {
	@Test
	fun getTitleResourceId() {
		assertThat(SettingsExperimentalFeaturesFragment().getTitleResourceId()).isEqualTo(R.string.experimental_features)
	}

	@Test
	fun getXmlResourceFile() {
		assertThat(SettingsExperimentalFeaturesFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_experimental_features)
	}
}
