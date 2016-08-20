package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.annotation.XmlRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v7.app.AlertDialog
import android.support.v7.preference.EditTextPreference
import android.support.v7.preference.ListPreference
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceGroup
import com.mgaetan89.showsrage.BuildConfig
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getLanguage
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getServerAddress
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat

// Code to display preferences values from: http://stackoverflow.com/a/18807490/1914223
open class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = this.activity

        if (activity is MainActivity) {
            activity.displayHomeAsUp(true)
        }
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
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
            val serverAddress = this.context.getPreferences().getServerAddress()

            this.setScreensIcon()

            if (serverAddress.isEmpty()) {
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

    // This can be set directly in XML when we support API 21+
    // Or when the Support Library supports it
    private fun setScreensIcon() {
        val screens = mapOf(
                "screen_server" to R.drawable.ic_wifi_24dp,
                "screen_display" to R.drawable.ic_device_android_24dp,
                "screen_behavior" to R.drawable.ic_widgets_24dp,
                "screen_experimental_features" to R.drawable.ic_explore_24dp,
                "screen_about" to R.drawable.ic_help_outline_24dp
        )

        screens.forEach {
            val icon = VectorDrawableCompat.create(this.resources, it.value, this.activity.theme)

            this.findPreference(it.key)?.icon = icon
        }
    }

    private fun setupDisplayLanguage(preference: Preference) {
        val preferences = this.context.getPreferences()
        val preferredLocale = preferences.getLocale()

        if (preference is ListPreference) {
            preference.entries = Constants.SUPPORTED_LOCALES.map { it.displayLanguage.capitalize() }.toTypedArray()
            preference.entryValues = Constants.SUPPORTED_LOCALES.map { it.language }.toTypedArray()

            if (preferences.getLanguage().isEmpty()) {
                preference.value = preferredLocale.language
            }
        }

        preference.summary = preferredLocale.displayLanguage.capitalize()
    }

    private fun updatePreference(preference: Preference?) {
        if (preference is EditTextPreference) {
            val text = preference.text

            if (Fields.SERVER_PASSWORD.field.equals(preference.key) && !text.isNullOrEmpty()) {
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
                    Fields.DISPLAY_LANGUAGE.field -> this.setupDisplayLanguage(preference)
                    else -> this.updatePreference(preference)
                }
            }
        }
    }
}
