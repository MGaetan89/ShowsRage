package com.mgaetan89.showsrage.model;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.mgaetan89.showsrage.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
	@NonNull
	private static final Pattern PATTERN = Pattern.compile("^((?:[0-9]{4}-[0-9]{2}-[0-9]{2}\\s+)?[0-9]{2}:[0-9]{2}:[0-9]{2})\\s+([A-Z]+)\\s+(.*)$");

	private String dateTime = "";

	private String errorType = "";

	private String message = "";

	public LogEntry(CharSequence log) {
		if (log != null) {
			Matcher matcher = PATTERN.matcher(log);

			if (matcher.matches()) {
				this.dateTime = matcher.group(1);
				this.errorType = matcher.group(2);
				this.message = matcher.group(3);
			}
		}
	}

	public String getDateTime() {
		return this.dateTime;
	}

	@ColorRes
	public int getErrorColor() {
		if (this.errorType != null) {
			String normalizedErrorType = this.errorType.toLowerCase();

			switch (normalizedErrorType) {
				case "debug":
					return R.color.green;

				case "error":
					return R.color.red;

				case "info":
					return R.color.blue;

				case "warning":
					return R.color.orange;
			}
		}

		return android.R.color.black;
	}

	public String getErrorType() {
		return this.errorType;
	}

	public String getMessage() {
		return this.message;
	}
}
