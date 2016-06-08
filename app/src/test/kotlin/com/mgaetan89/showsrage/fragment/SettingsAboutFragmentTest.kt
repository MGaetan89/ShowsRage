package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsAboutFragmentTest {
    @Test
    fun getTitleResourceId() {
        assertThat(SettingsAboutFragment().getTitleResourceId()).isEqualTo(R.string.about)
    }

    @Test
    fun getXmlResourceFile() {
        assertThat(SettingsAboutFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_about)
    }
}
