package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R

class SettingsAboutLicensesFragment : SettingsAboutFragment() {
    override fun getTitleResourceId() = R.string.licenses

    override fun getXmlResourceFile() = R.xml.settings_about_licenses
}
