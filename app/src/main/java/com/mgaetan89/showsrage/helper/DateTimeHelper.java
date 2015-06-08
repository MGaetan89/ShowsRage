package com.mgaetan89.showsrage.helper;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class DateTimeHelper {
	public static CharSequence getRelativeDate(String dateTime, String format, long minResolution) {
		try {
			DateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
			Date date = formatter.parse(dateTime);

			return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), minResolution);
		} catch (ParseException exception) {
			return dateTime;
		}
	}
}
