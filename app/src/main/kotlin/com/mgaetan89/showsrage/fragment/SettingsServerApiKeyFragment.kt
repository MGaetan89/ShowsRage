package com.mgaetan89.showsrage.fragment

import android.preference.Preference
import android.preference.PreferenceManager
import android.preference.PreferenceScreen
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.ApiKey
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

open class SettingsServerApiKeyFragment : SettingsServerFragment() {
    override fun onPreferenceTreeClick(preferenceScreen: PreferenceScreen?, preference: Preference?): Boolean {
        if ("get_api_key_action".equals(preference?.key)) {
            this.getApiKey()

            return true
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference)
    }

    override fun getTitleResourceId() = R.string.api_key

    override fun getXmlResourceFile() = R.xml.settings_server_api_key

    private fun getApiKey() {
        SickRageApi.instance.init(PreferenceManager.getDefaultSharedPreferences(this.activity))

        val username = this.getPreferenceValue("server_username", null)
        val password = this.getPreferenceValue("server_password", null)

        SickRageApi.instance.services?.getApiKey(username, password, ApiKeyCallback(this))
    }

    private class ApiKeyCallback(fragment: SettingsFragment) : Callback<ApiKey> {
        private val fragmentReference: WeakReference<SettingsFragment>

        init {
            this.fragmentReference = WeakReference(fragment)
        }

        override fun failure(error: RetrofitError?) {
            this.showApiKeyResult(null)
        }

        override fun success(apiKey: ApiKey?, response: Response?) {
            if (apiKey?.success ?: false) {
                this.showApiKeyResult(apiKey?.apiKey)
            } else {
                this.showApiKeyResult(null)
            }
        }

        private fun setPreferenceValue(fragment: SettingsFragment, key: String, value: String) {
            val sharedPreferences = fragment.preferenceManager.sharedPreferences
            sharedPreferences.edit().putString(key, value).apply()
        }

        private fun showApiKeyResult(apiKey: String?) {
            val fragment = this.fragmentReference.get() ?: return
            val messageId: Int

            if (TextUtils.isEmpty(apiKey)) {
                messageId = R.string.get_api_key_error
            } else {
                this.setPreferenceValue(fragment, "api_key", apiKey!!)
                fragment.findPreference("api_key").summary = apiKey

                messageId = R.string.get_api_key_success
            }

            AlertDialog.Builder(fragment.activity)
                    .setCancelable(true)
                    .setMessage(messageId)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
        }
    }
}
