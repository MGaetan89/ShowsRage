package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.annotation.XmlRes
import android.support.v7.app.AlertDialog
import android.support.v7.preference.*
import com.mgaetan89.showsrage.BuildConfig
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity

// Code to display preferences values from: http://stackoverflow.com/a/18807490/1914223
open class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = this.activity

        if (activity is MainActivity) {
            activity.displayHomeAsUp(true)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, s: String?) {
        this.addPreferencesFromResource(this.getXmlResourceFile())
        this.preferenceScreen?.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        this.preferenceScreen?.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)

        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()

        this.activity.setTitle(this.getTitleResourceId())

        if ("SettingsFragment".equals(this.javaClass.simpleName)) {
            val serverAddress = this.getPreferenceValue("server_address", "")

            if (serverAddress.isNullOrEmpty()) {
                AlertDialog.Builder(this.activity)
                        .setIcon(R.drawable.ic_notification)
                        .setTitle(R.string.app_name)
                        .setMessage(R.string.welcome_message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
            }
        }

        this.updatePreferenceGroup(this.preferenceScreen)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        this.updatePreference(this.findPreference(key))
    }

    protected fun getPreferenceValue(key: String, defaultValue: String?): String? {
        return this.preferenceManager.sharedPreferences?.getString(key, defaultValue)
    }

    @StringRes
    internal open fun getTitleResourceId() = R.string.settings

    @XmlRes
    internal open fun getXmlResourceFile() = R.xml.settings

    private fun updatePreference(preference: Preference?) {
        if (preference is EditTextPreference) {
            val text = preference.text

            if ("server_password".equals(preference.key) && !text.isNullOrEmpty()) {
                preference.summary = "*****"
            } else {
                preference.summary = text
            }
        } else if (preference is ListPreference) {
            preference.summary = preference.entry
        }
    }

    private fun updatePreferenceGroup(preferenceGroup: PreferenceGroup?) {
        if (preferenceGroup == null) {
            return
        }

        for (i in 0..(preferenceGroup.preferenceCount - 1)) {
            val preference = preferenceGroup.getPreference(i)

            if (preference is PreferenceGroup) {
                when (preference.key) {
                    "application_version" -> preference.summary = BuildConfig.VERSION_NAME
                    "get_api_key" -> preference.summary = this.getPreferenceValue("api_key", "")
                }

                this.updatePreferenceGroup(preference)
            } else {
                this.updatePreference(preference)
            }
        }
    }
}
