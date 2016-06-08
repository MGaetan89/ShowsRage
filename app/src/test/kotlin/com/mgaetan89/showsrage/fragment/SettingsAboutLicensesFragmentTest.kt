package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsAboutLicensesFragmentTest {
    @Test
    fun getTitleResourceId() {
        assertThat(SettingsAboutLicensesFragment().getTitleResourceId()).isEqualTo(R.string.licenses)
    }

    @Test
    fun getXmlResourceFile() {
        assertThat(SettingsAboutLicensesFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_about_licenses)
    }
}
