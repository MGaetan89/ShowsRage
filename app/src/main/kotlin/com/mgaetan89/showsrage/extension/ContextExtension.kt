package com.mgaetan89.showsrage.extension

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View

fun Context.getPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

fun Context.inflate(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(this).inflate(layoutId, null)
}
