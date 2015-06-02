package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
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

public class SettingsFragment extends PreferenceFragment implements Callback<GenericResponse> {
	@Nullable
	private AlertDialog alertDialog = null;

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
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.settings, menu);
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
	public void success(GenericResponse genericResponse, Response response) {
		this.showResult(true);
	}

	private void showResult(boolean successful) {
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
		SickRageApi.getInstance().init(this.getActivity());

		this.alertDialog = new AlertDialog.Builder(this.getActivity())
				.setCancelable(true)
				.setTitle(R.string.testing_server_settings)
				.setMessage(this.getString(R.string.connecting_to, SickRageApi.getInstance().getApiUrl()))
				.setNegativeButton(R.string.cancel, null)
				.show();

		SickRageApi.getInstance().getServices().ping(this);
	}
}
