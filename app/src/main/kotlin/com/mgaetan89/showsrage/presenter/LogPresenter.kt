package com.mgaetan89.showsrage.presenter

import android.support.annotation.ColorRes
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.humanize
import com.mgaetan89.showsrage.model.LogEntry

class LogPresenter(val logEntry: LogEntry?) {
	fun getDateTime(): CharSequence? {
		if (!this.isLogEntryValid()) {
			return ""
		}

		return DateTimeHelper.getRelativeDate(this.logEntry!!.dateTime, "yyyy-MM-dd hh:mm:ss", 0)
	}

	@ColorRes
	fun getErrorColor() = if (this.isLogEntryValid()) this.logEntry!!.getErrorColor() else android.R.color.white

	fun getErrorType() = if (this.isLogEntryValid()) this.logEntry!!.errorType ?: "" else ""

	fun getGroup() = if (this.isLogEntryValid()) this.logEntry!!.group?.humanize() else null

	fun getMessage() = if (this.isLogEntryValid()) this.logEntry!!.message.trim() else ""

	internal fun isLogEntryValid() = this.logEntry != null && this.logEntry.isValid
}
