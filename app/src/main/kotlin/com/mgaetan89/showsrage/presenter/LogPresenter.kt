package com.mgaetan89.showsrage.presenter

import android.support.annotation.ColorRes
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.humanize
import com.mgaetan89.showsrage.model.LogEntry

class LogPresenter(val logEntry: LogEntry?) {
    fun getDateTime(): CharSequence? {
        if (this.logEntry == null) {
            return ""
        }

        return DateTimeHelper.getRelativeDate(this.logEntry.dateTime, "yyyy-MM-dd hh:mm:ss", 0)
    }

    @ColorRes
    fun getErrorColor() = this.logEntry?.getErrorColor() ?: android.R.color.white

    fun getErrorType() = this.logEntry?.errorType ?: ""

    fun getGroup() = this.logEntry?.group?.humanize()

    fun getMessage() = this.logEntry?.message?.trim() ?: ""
}
