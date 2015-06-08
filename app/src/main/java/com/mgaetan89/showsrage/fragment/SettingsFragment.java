package com.mgaetan89.showsrage.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.mgaetan89.showsrage.R;
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
		this.showResult(false);
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
	public void onResume() {
		super.onResume();

		for (int i = 0; i < this.getPreferenceScreen().getPreferenceCount(); i++) {
			Preference preference = this.getPreferenceScreen().getPreference(i);

			if (preference instanceof PreferenceGroup) {
				PreferenceGroup preferenceGroup = (PreferenceGroup) preference;

				for (int j = 0; j < preferenceGroup.getPreferenceCount(); j++) {
					this.updatePreference(preferenceGroup.getPreference(j));
				}
			} else {
				this.updatePreference(preference);
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		this.updatePreference(this.findPreference(key));
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		this.showResult(true);
	}

	private void showResult(boolean successful) {
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

		SickRageApi.getInstance().init(this.getActivity());

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

	private void updatePreference(Preference preference) {
		if (preference instanceof EditTextPreference) {
			EditTextPreference editTextPreference = (EditTextPreference) preference;
			editTextPreference.setSummary(editTextPreference.getText());
		}
	}
}
