package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R

class SettingsExperimentalFeaturesFragment : SettingsFragment() {
    override fun getTitleResourceId() = R.string.experimental_features

    override fun getXmlResourceFile() = R.xml.settings_experimental_features
}
