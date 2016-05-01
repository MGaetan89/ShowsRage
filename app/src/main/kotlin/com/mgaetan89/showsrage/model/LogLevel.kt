package com.mgaetan89.showsrage.model

import java.util.*

enum class LogLevel {
    DEBUG,
    ERROR,
    INFO,
    WARNING;

    override fun toString(): String {
        return super.toString().toLowerCase(Locale.getDefault())
    }
}
