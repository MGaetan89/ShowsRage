package com.mgaetan89.showsrage.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
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

public class SettingsNetworkFragment extends SettingsFragment implements Callback<GenericResponse> {
	@Nullable
	private AlertDialog alertDialog = null;

	private boolean canceled = false;

	public SettingsNetworkFragment() {
		this.setHasOptionsMenu(true);
	}

	@Override
	public void failure(RetrofitError error) {
		this.showTestResult(false);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.settings_network, menu);
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
		this.showTestResult(true);
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.server;
	}

	@Override
	protected int getXmlResourceFile() {
		return R.xml.settings_network;
	}

	private void showTestResult(boolean successful) {
		if (this.canceled) {
			return;
		}

		if (this.alertDialog != null) {
			if (this.alertDialog.isShowing()) {
				this.alertDialog.dismiss();
			}

			this.alertDialog = null;
		}

		String url = SickRageApi.Companion.getInstance().getApiUrl();

		new AlertDialog.Builder(this.getActivity())
				.setCancelable(true)
				.setMessage(successful ? this.getString(R.string.connection_successful) : this.getString(R.string.connection_failed, url))
				.setPositiveButton(android.R.string.ok, null)
				.show();
	}

	private void testConnection() {
		Activity activity = this.getActivity();

		if (activity == null) {
			return;
		}

		this.canceled = false;

		SickRageApi.Companion.getInstance().init(PreferenceManager.getDefaultSharedPreferences(activity));

		this.alertDialog = new AlertDialog.Builder(activity)
				.setCancelable(true)
				.setTitle(R.string.testing_server_settings)
				.setMessage(this.getString(R.string.connecting_to, SickRageApi.Companion.getInstance().getApiUrl()))
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SettingsNetworkFragment.this.canceled = true;

						dialog.dismiss();
					}
				})
				.show();

		SickRageApi.Companion.getInstance().getServices().ping(this);
	}
}
