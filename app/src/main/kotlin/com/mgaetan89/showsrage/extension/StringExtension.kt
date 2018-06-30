package com.mgaetan89.showsrage.extension

import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

fun String.humanize(): String {
	return this.split(' ', '-').joinToString(" ") {
		if (it.isEmpty()) {
			it
		} else {
			it.first().toUpperCase() + it.drop(1).toLowerCase()
		}
	}
}

fun String.toLocale(): Locale? {
	if (this.isEmpty()) {
		return null
	}

	val defaultLocale = Locale.getDefault()

	Locale.setDefault(Locale.ENGLISH)

	val locale = Locale.getAvailableLocales().filter { locale ->
		locale.displayLanguage.startsWith(this, true)
	}.firstOrNull { it != null }

	Locale.setDefault(defaultLocale)

	return locale
}

fun String?.toRelativeDate(format: String, minResolution: Long): CharSequence {
	if (this.isNullOrEmpty()) {
		return "N/A"
	}

	return try {
		val date = SimpleDateFormat(format, Locale.getDefault()).parse(this)

		DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), minResolution)
	} catch (exception: ParseException) {
		this.orEmpty()
	}
}

fun String.toTimestamp(format: String) = try {
	// SickRage returns the air time in the american time format
	SimpleDateFormat(format, Locale.US).parse(this).time
} catch (exception: ParseException) {
	0L
}
