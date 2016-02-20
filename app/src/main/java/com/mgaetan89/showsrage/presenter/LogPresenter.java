package com.mgaetan89.showsrage.presenter;

import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.LogEntry;

public class LogPresenter {
	@Nullable
	private LogEntry logEntry;

	public LogPresenter(@Nullable LogEntry logEntry) {
		this.logEntry = logEntry;
	}

	public CharSequence getDateTime() {
		if (this.logEntry == null) {
			return "";
		}

		return DateTimeHelper.INSTANCE.getRelativeDate(this.logEntry.getDateTime(), "yyyy-MM-dd hh:mm:ss", 0);
	}

	@ColorRes
	public int getErrorColor() {
		if (this.logEntry == null) {
			return android.R.color.white;
		}

		return this.logEntry.getErrorColor();
	}

	public String getErrorType() {
		if (this.logEntry == null) {
			return "";
		}

		return this.logEntry.getErrorType();
	}

	public String getMessage() {
		if (this.logEntry == null) {
			return "";
		}

		return this.logEntry.getMessage().trim();
	}
}
