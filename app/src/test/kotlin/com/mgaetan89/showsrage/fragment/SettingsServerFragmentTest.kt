package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsServerFragmentTest {
	@Test
	fun getTitleResourceId() {
		assertThat(SettingsServerFragment().getTitleResourceId()).isEqualTo(R.string.server)
	}

	@Test
	fun getXmlResourceFile() {
		assertThat(SettingsServerFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_server)
	}
}
