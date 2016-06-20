package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.changeLocale

class SettingsDisplayFragment : SettingsFragment() {
    override fun getTitleResourceId() = R.string.display

    override fun getXmlResourceFile() = R.xml.settings_display

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        when (key) {
            "display_language" -> this.changeLanguage(sharedPreferences)
            "display_theme" -> this.changeTheme(sharedPreferences)
        }
    }

    private fun changeLanguage(sharedPreferences: SharedPreferences?) {
        val newLocale = getPreferredLocale(sharedPreferences?.getString("display_language", ""))

        this.resources.changeLocale(newLocale)

        this.activity.recreate()
    }

    private fun changeTheme(sharedPreferences: SharedPreferences?) {
        if (sharedPreferences?.getBoolean("display_theme", true) ?: true) {
            (this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            (this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        this.activity.recreate()
    }
}
