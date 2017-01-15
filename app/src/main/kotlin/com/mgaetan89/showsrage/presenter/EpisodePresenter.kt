package com.mgaetan89.showsrage.presenter

import android.support.annotation.ColorRes
import android.text.format.DateUtils
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.Episode

class EpisodePresenter(val episode: Episode?) {
    fun getAirDate(): CharSequence? {
        val airDate = this._getEpisode()?.airDate ?: return null

        if (airDate.isNullOrEmpty()) {
            return null
        }

        return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
    }

    fun getQuality(): String {
        val quality = this._getEpisode()?.quality ?: return ""

        return if ("N/A".equals(quality, true)) "" else quality
    }

    @ColorRes
    fun getStatusColor() = this._getEpisode()?.getStatusBackgroundColor() ?: android.R.color.transparent

    private fun _getEpisode() = if (this.episode?.isValid ?: false) this.episode else null
}
