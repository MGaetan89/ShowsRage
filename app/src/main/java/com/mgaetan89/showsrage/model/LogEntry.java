package com.mgaetan89.showsrage.model;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;

import com.mgaetan89.showsrage.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LogEntry extends RealmObject {
	@NonNull
	private static final Pattern PATTERN = Pattern.compile("^((?:[0-9]{4}-[0-9]{2}-[0-9]{2}\\s+)?[0-9]{2}:[0-9]{2}:[0-9]{2})\\s+([A-Z]+)\\s+([A-Z0-9-]+(?:\\s+::\\s+))?(.*)$");

	@PrimaryKey
	private String dateTime = "";

	private String errorType = "";

	private String group = "";

	private String message = "";

	public LogEntry() {
	}

	public LogEntry(CharSequence log) {
		if (log != null) {
			Matcher matcher = PATTERN.matcher(log);

			if (matcher.matches()) {
				this.dateTime = matcher.group(1);
				this.errorType = matcher.group(2);
				this.group = matcher.group(3);
				this.message = matcher.group(4);

				if (this.group != null) {
					int limit = this.group.indexOf(' ');

					if (limit < this.group.length()) {
						this.group = this.group.substring(0, limit);
					}
				}
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

	public String getGroup() {
		return this.group;
	}

	public String getMessage() {
		return this.message;
	}
}
