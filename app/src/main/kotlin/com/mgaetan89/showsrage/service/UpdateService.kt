package com.mgaetan89.showsrage.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.provider.Settings
import android.support.annotation.StringRes
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class UpdateService : Service(), Callback<GenericResponse>, Runnable {
	private val RETRY_INTERVAL = 10000L
	private val handler = Handler()
	private var updating = false

	override fun failure(error: RetrofitError?) {
		error?.printStackTrace()
	}

	override fun onBind(intent: Intent?) = null

	override fun onCreate() {
		super.onCreate()

		SickRageApi.instance.init(this.getPreferences())

		this.sendNotification(R.string.updating_sickrage, null)

		this.handler.postDelayed(this, RETRY_INTERVAL)
	}

	override fun run() {
		if (this.updating) {
			SickRageApi.instance.services?.ping(this)
		} else {
			SickRageApi.instance.services?.restart(this)
		}

		this.handler.postDelayed(this, RETRY_INTERVAL)
	}

	override fun success(genericResponse: GenericResponse?, response: Response?) {
		val isSuccess = "success".equals(genericResponse?.result, true)

		// We were updating, it's time to restart
		// If the update failed, we don't restart and display an error message
		if (this.updating && isSuccess) {
			this.updating = false

			return
		}

		val messageRes = if (isSuccess) R.string.sickrage_updated else R.string.update_failed
		val intent = Intent(this, MainActivity::class.java)
		val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

		this.sendNotification(messageRes, pendingIntent)

		this.handler.removeCallbacks(this, null)

		this.stopSelf()
	}

	private fun sendNotification(@StringRes text: Int, pendingIntent: PendingIntent?) {
		val channel = this.getString(R.string.app_name)
		val notification = NotificationCompat.Builder(this, channel)
				.setAutoCancel(true)
				.setColor(ContextCompat.getColor(this, R.color.primary))
				.setContentIntent(pendingIntent)
				.setContentTitle(this.getString(R.string.app_name))
				.setContentText(this.getString(text))
				.setLocalOnly(true)
				.setProgress(0, 0, true)
				.setSmallIcon(R.drawable.ic_notification)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
				.build()
		val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		notificationManager.notify(0, notification)
	}
}
