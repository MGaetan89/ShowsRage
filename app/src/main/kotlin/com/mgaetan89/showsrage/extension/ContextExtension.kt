package com.mgaetan89.showsrage.extension

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

fun Context.getPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
