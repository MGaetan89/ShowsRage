package com.mgaetan89.showsrage.presenter

import android.support.annotation.ColorRes
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.LogEntry

class LogPresenter(val logEntry: LogEntry?) {
    fun getDateTime(): CharSequence? {
        if (this.logEntry == null) {
            return ""
        }

        return DateTimeHelper.getRelativeDate(this.logEntry.dateTime, "yyyy-MM-dd hh:mm:ss", 0)
    }

    @ColorRes
    fun getErrorColor() = this.logEntry?.errorColor ?: android.R.color.white

    fun getErrorType() = this.logEntry?.errorType ?: ""

    fun getMessage(): String {
        val group = this.logEntry?.group
        val message = this.logEntry?.message

        if (!group.isNullOrEmpty()) {
            return group + " :: " + message
        }

        return message?.trim() ?: ""
    }
}
