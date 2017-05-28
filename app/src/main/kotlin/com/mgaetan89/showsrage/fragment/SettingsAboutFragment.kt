package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R

open class SettingsAboutFragment : SettingsFragment() {
	override fun getTitleResourceId() = R.string.about

	override fun getXmlResourceFile() = R.xml.settings_about

	companion object {
		fun newInstance() = SettingsAboutFragment()
	}
}
