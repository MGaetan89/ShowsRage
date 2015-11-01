package com.mgaetan89.showsrage.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class DateTimeHelper {
	public static CharSequence getRelativeDate(String dateTime, String format, long minResolution) {
		if (TextUtils.isEmpty(dateTime)) {
			return "N/A";
		}

		try {
			DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
			Date date = formatter.parse(dateTime);

			return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), minResolution);
		} catch (ParseException exception) {
			return dateTime;
		}
	}

	@Nullable
	public static String getLocalizedTime(Context context, String dateTime, String format) {
		long timestamp = getTimestamp(dateTime, format);

		if (timestamp == 0L) {
			return null;
		}

		return DateUtils.formatDateTime(context, timestamp, DateUtils.FORMAT_SHOW_TIME);
	}

	private static long getTimestamp(String dateTime, String format) {
		// SickRage returns the air time in the american time format
		DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

		try {
			Date date = dateFormat.parse(dateTime);

			return date.getTime();
		} catch (ParseException exception) {
			return 0L;
		}
	}
}
