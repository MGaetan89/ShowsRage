package com.mgaetan89.showsrage.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.model.UpdateResponse;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.mgaetan89.showsrage.service.UpdateService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateActivity extends AppCompatActivity implements Callback<GenericResponse>, DialogInterface.OnClickListener {
	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		this.finish();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				this.updateSickRage();

				break;

			case DialogInterface.BUTTON_NEGATIVE:
				this.finish();

				break;
		}
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		Toast.makeText(this, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();

		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SickRageApi.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this));

		UpdateResponse update = this.getIntent().getExtras().getParcelable(Constants.Bundle.UPDATE_MODEL);

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setTitle(R.string.update_sickrage);

		if (update != null) {
			builder.setMessage(this.getString(
					R.string.update_available_detailed,
					update.getCurrentVersion().getVersion(),
					update.getLatestVersion().getVersion(),
					update.getCommitsOffset()
			));
		}

		builder.setPositiveButton(R.string.update, this)
				.setNegativeButton(R.string.ignore, this)
				.show();
	}

	private void updateSickRage() {
		SickRageApi.getInstance().getServices().updateSickRage(this);

		this.startService(new Intent(this, UpdateService.class));

		Toast.makeText(this, R.string.updating_sickrage, Toast.LENGTH_SHORT).show();

		this.finish();
	}
}
