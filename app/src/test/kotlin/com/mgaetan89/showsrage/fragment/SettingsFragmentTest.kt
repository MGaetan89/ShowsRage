package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsFragmentTest {
	@Test
	fun getTitleResourceId() {
		assertThat(SettingsFragment.newInstance().getTitleResourceId()).isEqualTo(R.string.settings)
	}

	@Test
	fun getXmlResourceFile() {
		assertThat(SettingsFragment.newInstance().getXmlResourceFile()).isEqualTo(R.xml.settings)
	}
}
