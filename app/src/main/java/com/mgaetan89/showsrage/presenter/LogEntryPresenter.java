package com.mgaetan89.showsrage.presenter;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.View;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.LogEntry;

public class LogEntryPresenter {
	@Nullable
	private LogEntry logEntry = null;

	@Nullable
	private View view = null;

	public LogEntryPresenter(@Nullable LogEntry logEntry, @Nullable View view) {
		this.logEntry = logEntry;
		this.view = view;
	}

	public CharSequence getDateTime() {
		if (this.logEntry == null) {
			return "";
		}

		return DateTimeHelper.getRelativeDate(this.logEntry.getDateTime(), "yyyy-MM-dd hh:mm:ss", 0);
	}

	@ColorInt
	public int getErrorColor() {
		if (this.logEntry == null) {
			if (this.view != null) {
				return this.view.getResources().getColor(android.R.color.black);
			}

			return Color.BLACK;
		}

		if (this.view != null) {
			return this.view.getResources().getColor(this.logEntry.getErrorColor());
		}

		return Color.BLACK;
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

		String message = this.logEntry.getMessage();

		if (message == null) {
			return "";
		}

		return message.trim();
	}
}
