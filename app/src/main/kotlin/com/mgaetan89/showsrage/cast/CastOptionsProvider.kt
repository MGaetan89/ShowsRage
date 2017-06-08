package com.mgaetan89.showsrage.cast

import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider

class CastOptionsProvider : OptionsProvider {
	override fun getCastOptions(context: Context?): CastOptions {
		val castOptions = CastOptions.Builder()
				.setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
				.build()

		return castOptions
	}

	override fun getAdditionalSessionProviders(context: Context?) = null
}
