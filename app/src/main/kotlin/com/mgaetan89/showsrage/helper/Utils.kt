package com.mgaetan89.showsrage.helper

import android.graphics.Color
import android.support.annotation.ColorInt

object Utils {
    fun getContrastColor(@ColorInt color: Int): Int {
        val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000

        return if (y >= 128) Color.BLACK else Color.WHITE
    }
}
