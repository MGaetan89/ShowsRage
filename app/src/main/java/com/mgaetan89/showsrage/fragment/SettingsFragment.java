package com.mgaetan89.showsrage.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.mgaetan89.showsrage.BuildConfig;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.BaseActivity;

// Code to display preferences values from: http://stackoverflow.com/a/18807490/1914223
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	public SettingsFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Activity activity = this.getActivity();

		if (activity instanceof BaseActivity) {
			((BaseActivity) activity).displayHomeAsUp(true);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.addPreferencesFromResource(this.getXmlResourceFile());
		this.getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onDestroy() {
		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();

		this.getActivity().setTitle(this.getTitleResourceId());

		if ("SettingsFragment".equals(this.getClass().getSimpleName())) {
			String serverAddress = this.getPreferenceValue("server_address", "");

			if (TextUtils.isEmpty(serverAddress)) {
				new AlertDialog.Builder(this.getActivity())
						.setIcon(R.drawable.ic_notification)
						.setTitle(R.string.app_name)
						.setMessage(R.string.welcome_message)
						.setPositiveButton(android.R.string.ok, null)
						.show();
			}
		}

		this.updatePreferenceGroup(this.getPreferenceScreen());
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		updatePreference(this.findPreference(key));
	}

	protected String getPreferenceValue(String key, String defaultValue) {
		return this.getPreferenceManager().getSharedPreferences().getString(key, defaultValue);
	}

	@StringRes
	protected int getTitleResourceId() {
		return R.string.settings;
	}

	@XmlRes
	protected int getXmlResourceFile() {
		return R.xml.settings;
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
		} else if (preference instanceof ListPreference) {
			preference.setSummary(((ListPreference) preference).getEntry());
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
