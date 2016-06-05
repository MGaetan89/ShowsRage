package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SettingsBehaviorFragmentTest {
    @Test
    fun getTitleResourceId() {
        assertThat(SettingsBehaviorFragment().getTitleResourceId()).isEqualTo(R.string.behavior)
    }

    @Test
    fun getXmlResourceFile() {
        assertThat(SettingsBehaviorFragment().getXmlResourceFile()).isEqualTo(R.xml.settings_behavior)
    }
}
