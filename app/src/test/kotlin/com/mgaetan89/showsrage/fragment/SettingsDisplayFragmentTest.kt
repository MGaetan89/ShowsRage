package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsDisplayFragmentTest {
	@Test
	fun getTitleResourceId() {
		assertThat(SettingsDisplayFragment.newInstance().getTitleResourceId()).isEqualTo(R.string.display)
	}

	@Test
	fun getXmlResourceFile() {
		assertThat(SettingsDisplayFragment.newInstance().getXmlResourceFile()).isEqualTo(R.xml.settings_display)
	}
}
