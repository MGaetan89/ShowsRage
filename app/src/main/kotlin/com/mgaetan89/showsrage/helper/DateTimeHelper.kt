package com.mgaetan89.showsrage.helper

import android.content.Context
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeHelper {
	fun getRelativeDate(dateTime: String?, format: String, minResolution: Long): CharSequence? {
		if (dateTime.isNullOrEmpty()) {
			return "N/A"
		}

		try {
			val date = SimpleDateFormat(format, Locale.getDefault()).parse(dateTime)

			return DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), minResolution)
		} catch (exception: ParseException) {
			return dateTime!!
		}
	}

	fun getLocalizedTime(context: Context, dateTime: String, format: String): String? {
		val timestamp = this.getTimestamp(dateTime, format).takeIf { it > 0L } ?: return null

		return DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_TIME)
	}

	private fun getTimestamp(dateTime: String, format: String): Long {
		return try {
			// SickRage returns the air time in the american time format
			SimpleDateFormat(format, Locale.US).parse(dateTime).time
		} catch (exception: ParseException) {
			0L
		}
	}
}
