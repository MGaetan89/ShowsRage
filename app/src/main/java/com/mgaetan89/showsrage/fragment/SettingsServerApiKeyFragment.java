package com.mgaetan89.showsrage.fragment;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.ApiKey;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsServerApiKeyFragment extends SettingsServerFragment {
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, @NonNull Preference preference) {
		if ("get_api_key_action".equals(preference.getKey())) {
			this.getApiKey();

			return true;
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.api_key;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_server_api_key;
	}

	private void getApiKey() {
		SickRageApi.Companion.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));

		String username = this.getPreferenceValue("server_username", null);
		String password = this.getPreferenceValue("server_password", null);

		SickRageApi.Companion.getInstance().getServices().getApiKey(username, password, new ApiKeyCallback(this));
	}

	private static final class ApiKeyCallback implements Callback<ApiKey> {
		private WeakReference<SettingsFragment> fragmentReference = null;

		private ApiKeyCallback(SettingsFragment fragment) {
			this.fragmentReference = new WeakReference<>(fragment);
		}

		@Override
		public void failure(RetrofitError error) {
			this.showApiKeyResult(null);
		}

		@Override
		public void success(ApiKey apiKey, Response response) {
			if (apiKey.isSuccess()) {
				this.showApiKeyResult(apiKey.getApiKey());
			} else {
				this.showApiKeyResult(null);
			}
		}

		private void setPreferenceValue(SettingsFragment fragment, String key, String value) {
			SharedPreferences sharedPreferences = fragment.getPreferenceManager().getSharedPreferences();
			sharedPreferences.edit().putString(key, value).apply();
		}

		private void showApiKeyResult(@Nullable String apiKey) {
			SettingsFragment fragment = this.fragmentReference.get();

			if (fragment == null) {
				return;
			}

			int messageId;

			if (TextUtils.isEmpty(apiKey)) {
				messageId = R.string.get_api_key_error;
			} else {
				this.setPreferenceValue(fragment, "api_key", apiKey);
				fragment.findPreference("api_key").setSummary(apiKey);

				messageId = R.string.get_api_key_success;
			}

			new AlertDialog.Builder(fragment.getActivity())
					.setCancelable(true)
					.setMessage(messageId)
					.setPositiveButton(android.R.string.ok, null)
					.show();
		}
	}
}
