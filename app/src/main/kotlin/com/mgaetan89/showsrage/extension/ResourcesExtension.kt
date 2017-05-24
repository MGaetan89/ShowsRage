package com.mgaetan89.showsrage.extension

import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

fun Resources.changeLocale(locale: Locale) {
    Locale.setDefault(locale)

    val configuration = Configuration()
    configuration.locale = locale

    this.updateConfiguration(configuration, this.displayMetrics)
}
