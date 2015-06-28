package com.mgaetan89.showsrage.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mgaetan89.showsrage.BuildConfig;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.ApiKey;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

// Code to display preferences values from: http://stackoverflow.com/a/18807490/1914223
public class SettingsFragment extends PreferenceFragment implements Callback<GenericResponse>, SharedPreferences.OnSharedPreferenceChangeListener {
	@Nullable
	private AlertDialog alertDialog = null;

	private boolean canceled = false;

	public SettingsFragment() {
		this.setHasOptionsMenu(true);
	}

	@Override
	public void failure(RetrofitError error) {
		this.showTestResult(false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(R.xml.settings);
		this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.settings, menu);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_test:
				this.testConnection();

				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, @NonNull Preference preference) {
		if ("get_api_key_action".equals(preference.getKey())) {
			this.getApiKey();

			return true;
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public void onResume() {
		super.onResume();

		this.updatePreferenceGroup(this.getPreferenceScreen());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		updatePreference(this.findPreference(key));
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		this.showTestResult(true);
	}

	private void getApiKey() {
		SickRageApi.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));

		String username = this.getPreferenceValue("server_username", null);
		String password = this.getPreferenceValue("server_password", null);

		SickRageApi.getInstance().getServices().getApiKey(username, password, new Callback<ApiKey>() {
			@Override
			public void failure(RetrofitError error) {
				showApiKeyResult(null);
			}

			@Override
			public void success(ApiKey apiKey, Response response) {
				if (apiKey.isSuccess()) {
					showApiKeyResult(apiKey.getApiKey());
				} else {
					showApiKeyResult(null);
				}
			}
		});
	}

	private String getPreferenceValue(String key, String defaultValue) {
		return this.getPreferenceManager().getSharedPreferences().getString(key, defaultValue);
	}

	private void setPreferenceValue(String key, String value) {
		SharedPreferences sharedPreferences = this.getPreferenceManager().getSharedPreferences();
		sharedPreferences.edit().putString(key, value).apply();
	}

	private void showApiKeyResult(@Nullable String apiKey) {
		int messageId;

		if (TextUtils.isEmpty(apiKey)) {
			messageId = R.string.get_api_key_error;
		} else {
			this.setPreferenceValue("api_key", apiKey);
			this.findPreference("api_key").setSummary(apiKey);
			this.findPreference("get_api_key").setSummary(apiKey);

			messageId = R.string.get_api_key_success;
		}

		new AlertDialog.Builder(this.getActivity())
				.setCancelable(true)
				.setMessage(messageId)
				.setPositiveButton(android.R.string.ok, null)
				.show();
	}

	private void showTestResult(boolean successful) {
		if (this.canceled) {
			return;
		}

		if (this.alertDialog != null) {
			this.alertDialog.dismiss();

			this.alertDialog = null;
		}

		String url = SickRageApi.getInstance().getApiUrl();

		new AlertDialog.Builder(this.getActivity())
				.setCancelable(true)
				.setMessage(successful ? this.getString(R.string.connection_successful) : this.getString(R.string.connection_failed, url))
				.setPositiveButton(android.R.string.ok, null)
				.show();
	}

	private void testConnection() {
		this.canceled = false;

		SickRageApi.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this.getActivity()));

		this.alertDialog = new AlertDialog.Builder(this.getActivity())
				.setCancelable(true)
				.setTitle(R.string.testing_server_settings)
				.setMessage(this.getString(R.string.connecting_to, SickRageApi.getInstance().getApiUrl()))
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						canceled = true;

						dialog.dismiss();
					}
				})
				.show();

		SickRageApi.getInstance().getServices().ping(this);
	}

	private static void updatePreference(Preference preference) {
		if (preference instanceof EditTextPreference) {
			EditTextPreference editTextPreference = (EditTextPreference) preference;
			String key = editTextPreference.getKey();
			String text = editTextPreference.getText();

			if ("server_password".equals(key) && !TextUtils.isEmpty(text)) {
				editTextPreference.setSummary("*****");
			} else {
				editTextPreference.setSummary(text);
			}
		}
	}

	private void updatePreferenceGroup(PreferenceGroup preferenceGroup) {
		for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
			Preference preference = preferenceGroup.getPreference(i);

			if (preference instanceof PreferenceGroup) {
				String key = preference.getKey();

				if ("application_version".equals(key)) {
					preference.setSummary(BuildConfig.VERSION_NAME);
				} else if ("get_api_key".equals(key)) {
					preference.setSummary(this.getPreferenceValue("api_key", ""));
				}

				this.updatePreferenceGroup((PreferenceGroup) preference);
			} else {
				updatePreference(preference);
			}
		}
	}
}
