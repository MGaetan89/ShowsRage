package com.mgaetan89.showsrage.helper

import android.content.Context
import android.text.TextUtils
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeHelper {
    fun getRelativeDate(dateTime: String?, format: String, minResolution: Long): CharSequence {
        if (TextUtils.isEmpty(dateTime)) {
            return "N/A";
        }

        try {
            val formatter = SimpleDateFormat(format, Locale.getDefault())
            val date = formatter.parse(dateTime)

            return DateUtils.getRelativeTimeSpanString(date.time, System.currentTimeMillis(), minResolution)
        } catch(exception: ParseException) {
            return dateTime!!
        }
    }

    fun getLocalizedTime(context: Context, dateTime: String, format: String): String? {
        val timestamp = this.getTimestamp(dateTime, format)

        if (timestamp == 0L) {
            return null
        }

        return DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_TIME)
    }

    private fun getTimestamp(dateTime: String, format: String): Long {
        try {
            // SickRage returns the air time in the american time format
            val formatter = SimpleDateFormat(format, Locale.US)
            val date = formatter.parse(dateTime)

            return date.time
        } catch(exception: ParseException) {
            return 0L
        }
    }
}
