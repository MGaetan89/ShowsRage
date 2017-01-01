package com.mgaetan89.showsrage.helper

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.graphics.Palette
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ThemeColors
import io.realm.Realm
import io.realm.RealmConfiguration

object Utils {
    fun getContrastColor(@ColorInt color: Int): Int {
        val y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000

        return if (y >= 128) Color.BLACK else Color.WHITE
    }

    fun getSortableShowName(show: Show, ignoreArticles: Boolean): String {
        return if (ignoreArticles) {
            show.showName?.replaceFirst("^(?:an?|the)\\s+".toRegex(RegexOption.IGNORE_CASE), "")?.toLowerCase()
        } else {
            show.showName?.toLowerCase()
        } ?: ""
    }

    fun getThemeColors(context: Context, palette: Palette): ThemeColors {
        // Define the accent color
        val accentCandidates = arrayOf(
                palette.darkMutedSwatch, palette.mutedSwatch, palette.lightMutedSwatch
        )
        val accent = accentCandidates.firstOrNull { it != null }
        val accentColor = accent?.rgb ?: ContextCompat.getColor(context, R.color.accent)

        // Define the primary color
        val primaryCandidates = arrayOf(
                palette.vibrantSwatch, palette.lightVibrantSwatch, palette.darkVibrantSwatch,
                palette.lightMutedSwatch, palette.mutedSwatch, palette.darkMutedSwatch
        )
        val primary = primaryCandidates.firstOrNull { it != null }
        val primaryColor = primary?.rgb ?: ContextCompat.getColor(context, R.color.primary)

        return ThemeColors(primaryColor, accentColor)
    }

    fun initRealm(context: Context) {
        Realm.init(context)

        val configuration = RealmConfiguration.Builder().let {
            it.schemaVersion(Constants.DATABASE_VERSION)
            it.migration(Migration())
            it.build()
        }

        Realm.setDefaultConfiguration(configuration)
    }
}
