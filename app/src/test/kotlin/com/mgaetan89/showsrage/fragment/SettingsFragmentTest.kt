package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsFragmentTest {
	@Test
	fun getTitleResourceId() {
		assertThat(SettingsFragment().getTitleResourceId()).isEqualTo(R.string.settings)
	}

	@Test
	fun getXmlResourceFile() {
		assertThat(SettingsFragment().getXmlResourceFile()).isEqualTo(R.xml.settings)
	}
}
