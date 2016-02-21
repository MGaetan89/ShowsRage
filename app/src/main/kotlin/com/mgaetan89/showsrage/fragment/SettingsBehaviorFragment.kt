package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R

class SettingsBehaviorFragment : SettingsFragment() {
    override fun getTitleResourceId() = R.string.behavior

    override fun getXmlResourceFile() = R.xml.settings_behavior
}
