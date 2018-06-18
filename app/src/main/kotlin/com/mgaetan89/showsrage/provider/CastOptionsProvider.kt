package com.mgaetan89.showsrage.provider

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.NotificationOptions
import com.mgaetan89.showsrage.activity.MainActivity

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(appContext: Context?): CastOptions {
        val notificationOptions = NotificationOptions.Builder()
            .setTargetActivityClassName(MainActivity::class.java.name)
            .build()

        val mediaOptions = CastMediaOptions.Builder()
            .setNotificationOptions(notificationOptions)
            .build()

        return CastOptions.Builder()
            .setCastMediaOptions(mediaOptions)
            .build()
    }

    override fun getAdditionalSessionProviders(appContext: Context?) = null
}
