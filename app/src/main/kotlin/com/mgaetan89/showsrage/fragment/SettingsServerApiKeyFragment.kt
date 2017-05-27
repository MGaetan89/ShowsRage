package com.mgaetan89.showsrage.fragment

import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getServerPassword
import com.mgaetan89.showsrage.extension.getServerUsername
import com.mgaetan89.showsrage.model.ApiKey
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class SettingsServerApiKeyFragment : SettingsServerFragment() {
	override fun onPreferenceTreeClick(preference: Preference?): Boolean {
		if ("get_api_key_action" == preference?.key) {
			this.getApiKey()

			return true
		}

		return super.onPreferenceTreeClick(preference)
	}

	override fun getTitleResourceId() = R.string.api_key

	override fun getXmlResourceFile() = R.xml.settings_server_api_key

	private fun getApiKey() {
		val preferences = this.activity.getPreferences()

		SickRageApi.instance.init(preferences)

		val username = preferences.getServerUsername()
		val password = preferences.getServerPassword()

		SickRageApi.instance.services?.getApiKey(username, password, ApiKeyCallback(this))
	}

	private class ApiKeyCallback(fragment: SettingsFragment) : Callback<ApiKey> {
		private val fragmentReference = WeakReference(fragment)

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
			val activity = fragment.activity ?: return
			val messageId: Int

			if (apiKey.isNullOrEmpty()) {
				messageId = R.string.get_api_key_error
			} else {
				this.setPreferenceValue(fragment, Fields.API_KEY.field, apiKey!!)
				fragment.findPreference(Fields.API_KEY.field).summary = apiKey

				messageId = R.string.get_api_key_success
			}

			AlertDialog.Builder(activity)
					.setCancelable(true)
					.setMessage(messageId)
					.setPositiveButton(android.R.string.ok, null)
					.show()
		}
	}
}
