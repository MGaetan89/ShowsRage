package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R

class SettingsAboutShowsRageFragment : SettingsAboutFragment() {
	override fun getTitleResourceId() = R.string.app_name

	override fun getXmlResourceFile() = R.xml.settings_about_showsrage

	companion object {
		fun newInstance() = SettingsAboutShowsRageFragment()
	}
}
