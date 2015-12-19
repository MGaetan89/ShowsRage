package com.mgaetan89.showsrage.activity

import android.os.Bundle
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.*

class SettingsActivity : BaseActivity() {
    override fun displayHomeAsUp() = true

    override fun getFragment() = null

    override fun getSelectedMenuId() = R.id.menu_settings

    override fun getTitleResourceId() = R.string.settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val path = this.intent.data?.path

            this.fragmentManager.beginTransaction()
                    .replace(R.id.content, getFragmentForPath(path))
                    .commit()
        }
    }

    companion object {
        fun getFragmentForPath(path: String?): SettingsFragment {
            return when (path) {
                "/about" -> SettingsAboutFragment()
                "/about/licenses" -> SettingsAboutLicensesFragment()
                "/about/showsrage" -> SettingsAboutShowsRageFragment()
                "/behavior" -> SettingsBehaviorFragment()
                "/display" -> SettingsDisplayFragment()
                "/experimental_features" -> SettingsExperimentalFeaturesFragment()
                "/network" -> SettingsNetworkFragment()
                "/network/api_key" -> SettingsNetworkApiKeyFragment()
                else -> SettingsFragment()
            }
        }
    }
}
