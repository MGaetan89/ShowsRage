package com.mgaetan89.showsrage.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.activity.ShowsActivity;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UpdateService extends Service implements Callback<GenericResponse>, Runnable {
	private static final long RETRY_INTERVAL = 10000L;

	private final Handler handler = new Handler();

	private boolean updating = true;

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		SickRageApi.getInstance().init(PreferenceManager.getDefaultSharedPreferences(this));

		Notification notification = new NotificationCompat.Builder(this)
				.setAutoCancel(true)
				.setColor(ContextCompat.getColor(this, R.color.primary))
				.setContentTitle(this.getString(R.string.app_name))
				.setContentText(this.getString(R.string.updating_sickrage))
				.setLocalOnly(true)
				.setProgress(0, 0, true)
				.setSmallIcon(R.drawable.ic_notification)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
				.build();

		NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);

		this.handler.postDelayed(this, RETRY_INTERVAL);
	}

	@Override
	public void run() {
		if (this.updating) {
			SickRageApi.getInstance().getServices().ping(this);
		} else {
			SickRageApi.getInstance().getServices().restart(this);
		}

		this.handler.postDelayed(this, RETRY_INTERVAL);
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		boolean isSuccess = "success".equalsIgnoreCase(genericResponse.getResult());

		// We were updating, it's time to restart
		// If the update failed, we don't restart and display an error message
		if (this.updating && isSuccess) {
			this.updating = false;

			return;
		}

		int messageRes = R.string.update_failed;

		if (isSuccess) {
			messageRes = R.string.sickrage_updated;
		}

		Intent intent = new Intent(this, ShowsActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(this)
				.setAutoCancel(true)
				.setColor(ContextCompat.getColor(this, R.color.primary))
				.setContentIntent(pendingIntent)
				.setContentTitle(this.getString(R.string.app_name))
				.setContentText(this.getString(messageRes))
				.setLocalOnly(true)
				.setProgress(0, 0, false)
				.setSmallIcon(R.drawable.ic_notification)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
				.build();

		NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);

		this.handler.removeCallbacks(this, null);

		this.stopSelf();
	}
}
