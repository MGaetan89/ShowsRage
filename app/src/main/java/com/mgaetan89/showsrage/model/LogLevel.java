package com.mgaetan89.showsrage.model;

import java.util.Locale;

public enum LogLevel {
	DEBUG,
	ERROR,
	INFO,
	WARNING;

	@Override
	public String toString() {
		return super.toString().toLowerCase(Locale.getDefault());
	}
}
