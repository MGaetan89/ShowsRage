package com.mgaetan89.showsrage.model

import java.util.Locale

enum class LogLevel(val logLevels: Array<String>) {
	DEBUG(arrayOf("DEBUG", "ERROR", "INFO", "WARNING")),
	ERROR(arrayOf("ERROR")),
	INFO(arrayOf("ERROR", "INFO", "WARNING")),
	WARNING(arrayOf("ERROR", "WARNING"));

	override fun toString(): String {
		return super.toString().toLowerCase(Locale.getDefault())
	}
}
