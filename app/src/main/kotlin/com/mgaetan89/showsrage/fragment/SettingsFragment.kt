package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.annotation.XmlRes
import android.support.v7.app.AlertDialog
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceGroup
import com.mgaetan89.showsrage.BuildConfig
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import java.util.*

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

    private fun setupDisplayLanguage(preference: Preference) {
        val displayLanguage = this.getPreferenceValue("display_language", "")
        val preferredLocale = getPreferredLocale(displayLanguage)

        if (preference is ListPreference) {
            preference.entries = Constants.SUPPORTED_LOCALES.map { it.displayLanguage.capitalize() }.toTypedArray()
            preference.entryValues = Constants.SUPPORTED_LOCALES.map { it.language }.toTypedArray()

            if (displayLanguage.isNullOrEmpty()) {
                preference.value = preferredLocale.language
            }
        }

        preference.summary = preferredLocale.displayLanguage.capitalize()
    }

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
                when (preference.key) {
                    "display_language" -> this.setupDisplayLanguage(preference)
                    else -> this.updatePreference(preference)
                }
            }
        }
    }

    companion object {
        fun getPreferredLocale(language: String?): Locale {
            return when (language) {
                "en" -> Locale.ENGLISH
                "fr" -> Locale.FRENCH
                else -> if (Constants.SUPPORTED_LOCALES.contains(Locale.getDefault())) {
                    Locale.getDefault()
                } else {
                    Constants.DEFAULT_LOCALE
                }
            }
        }
    }
}
