package com.mgaetan89.showsrage.extension

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View

fun Context.getPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

fun Context.getLocalizedTime(dateTime: String, format: String): String? {
    val timestamp = dateTime.toTimestamp(format).takeIf { it > 0L } ?: return null

    return DateUtils.formatDateTime(this, timestamp, DateUtils.FORMAT_SHOW_TIME)
}

fun Context.inflate(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(this).inflate(layoutId, null)
}
