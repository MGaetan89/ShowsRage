package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.mgaetan89.showsrage.R

class SettingsDisplayFragment : SettingsFragment() {
    override fun getTitleResourceId() = R.string.display

    override fun getXmlResourceFile() = R.xml.settings_display

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)

        if ("display_theme".equals(key)) {
            if (sharedPreferences?.getBoolean("display_theme", true) ?: true) {
                (this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                (this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            this.activity.recreate()
        }
    }
}
