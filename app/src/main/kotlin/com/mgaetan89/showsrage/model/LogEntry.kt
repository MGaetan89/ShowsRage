package com.mgaetan89.showsrage.model

import android.support.annotation.ColorRes
import com.mgaetan89.showsrage.R
import io.realm.RealmObject
import io.realm.annotations.Index
import java.util.regex.Pattern

open class LogEntry(log: CharSequence? = null) : RealmObject() {
    open var dateTime: String = ""
    open var errorType: String? = ""
    @Index
    open var group: String? = ""
    open var message: String = ""

    init {
        if (!log.isNullOrEmpty()) {
            val matcher = PATTERN.matcher(log)

            if (matcher.matches()) {
                this.dateTime = matcher.group(1)
                this.errorType = matcher.group(2)
                this.group = matcher.group(3)
                this.message = matcher.group(4)

                if (!this.group.isNullOrEmpty()) {
                    val limit = this.group!!.indexOf(' ')

                    if (limit < this.group!!.length) {
                        this.group = this.group!!.substring(0, limit)
                    }
                }
            }
        }
    }

    @ColorRes
    fun getErrorColor(): Int {
        val normalizedErrorType = this.errorType?.toLowerCase()

        return when (normalizedErrorType) {
            "debug" -> R.color.green
            "error" -> R.color.red
            "info" -> R.color.blue
            "warning" -> R.color.orange
            else -> android.R.color.black
        }
    }

    companion object {
        private val PATTERN = Pattern.compile("^((?:[0-9]{4}-[0-9]{2}-[0-9]{2}\\s+)?[0-9]{2}:[0-9]{2}:[0-9]{2})\\s+([A-Z]+)\\s+([A-Z0-9-]+(?:\\s+::\\s+))?(.*)$")
    }
}
