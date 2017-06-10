package com.mgaetan89.showsrage.cast

import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.MediaIntentReceiver
import com.google.android.gms.cast.framework.media.NotificationOptions
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity

class CastOptionsProvider : OptionsProvider {
	override fun getCastOptions(context: Context?): CastOptions {
		val actions = listOf(MediaIntentReceiver.ACTION_SKIP_NEXT, MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK, MediaIntentReceiver.ACTION_STOP_CASTING)

		val notificationOptions = NotificationOptions.Builder()
				.setActions(actions, intArrayOf(1, 2))
				.setTargetActivityClassName(ExpandedControllerActivity::class.java.name)
				.build()

		val castMediaOptions = CastMediaOptions.Builder()
				.setNotificationOptions(notificationOptions)
				.build()

		val castOptions = CastOptions.Builder()
				.setCastMediaOptions(castMediaOptions)
				.setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
				.build()

		return castOptions
	}

	override fun getAdditionalSessionProviders(context: Context?) = null
}
