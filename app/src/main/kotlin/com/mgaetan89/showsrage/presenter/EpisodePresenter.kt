package com.mgaetan89.showsrage.presenter

import android.support.annotation.ColorRes
import android.text.format.DateUtils
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.Episode

class EpisodePresenter(val episode: Episode?) {
    fun getAirDate(): CharSequence? {
        if (this.episode == null) {
            return null
        }

        val airDate = this.episode.airDate

        if (airDate.isNullOrEmpty()) {
            return null
        }

        return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
    }

    fun getQuality(): String {
        if (this.episode == null) {
            return ""
        }

        val quality = this.episode.quality

        return if ("N/A".equals(quality, true)) "" else quality
    }

    @ColorRes
    fun getStatusColor() = this.episode?.statusBackgroundColor ?: android.R.color.transparent
}
