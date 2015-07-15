package com.mgaetan89.showsrage.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

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
				.setColor(this.getResources().getColor(R.color.primary))
				.setContentTitle(this.getString(R.string.app_name))
				.setContentText(this.getString(R.string.updating_sickrage))
				.setLocalOnly(true)
				.setProgress(0, 0, true)
				.setSmallIcon(R.mipmap.ic_launcher)
				.build();

		NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);

		this.handler.postDelayed(this, RETRY_INTERVAL);
	}

	@Override
	public void run() {
		SickRageApi.getInstance().getServices().ping(this);

		this.handler.postDelayed(this, RETRY_INTERVAL);
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		int messageRes = R.string.update_failed;

		if ("success".equalsIgnoreCase(genericResponse.getResult())) {
			messageRes = R.string.sickrage_updated;
		}

		Intent intent = new Intent(this, ShowsActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification notification = new NotificationCompat.Builder(this)
				.setAutoCancel(true)
				.setColor(this.getResources().getColor(R.color.primary))
				.setContentIntent(pendingIntent)
				.setContentTitle(this.getString(R.string.app_name))
				.setContentText(this.getString(messageRes))
				.setLocalOnly(true)
				.setProgress(0, 0, false)
				.setSmallIcon(R.mipmap.ic_launcher)
				.build();

		NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification);

		this.handler.removeCallbacks(this, null);

		this.stopSelf();
	}
}
