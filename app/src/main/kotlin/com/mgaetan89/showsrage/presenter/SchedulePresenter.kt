package com.mgaetan89.showsrage.presenter

import android.content.Context
import android.text.format.DateUtils
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.network.SickRageApi

class SchedulePresenter(val schedule: Schedule?, val context: Context?) {
    fun getAirDate(): CharSequence? {
        if (this.schedule == null) {
            return null
        }

        val airDate = this.schedule.airDate

        if (airDate.isNullOrEmpty()) {
            return null
        }

        return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
    }

    fun getAirDateTime(): CharSequence? {
        val airDate = this.getAirDate()
        val airTime = this.getAirTime()

        if (airDate == null && airTime == null) {
            return null
        }

        if (airDate != null) {
            return if (airTime != null) "$airDate, $airTime" else airDate
        }

        return airTime
    }

    fun getAirTime(): CharSequence? {
        if (this.context == null || this.schedule == null) {
            return null
        }

        val airDate = this.schedule.airDate
        val airTime = this.getAirTimeOnly()

        if (airDate.isNullOrEmpty() || airTime.isNullOrEmpty()) {
            return null
        }

        return DateTimeHelper.getLocalizedTime(this.context, "$airDate $airTime", "yyyy-MM-dd K:mm a")
    }

    fun getEpisode() = this.schedule?.episode ?: 0

    fun getNetwork() = this.schedule?.network ?: ""

    fun getPosterUrl() = if (this.schedule == null) "" else SickRageApi.instance.getPosterUrl(this.schedule.tvDbId, Indexer.TVDB)

    fun getQuality() = this.schedule?.quality ?: ""

    fun getSeason() = this.schedule?.season ?: 0

    fun getShowName() = this.schedule?.showName ?: ""

    internal fun getAirTimeOnly(): String? {
        if (this.schedule == null) {
            return null
        }

        val airTime = this.schedule.airs

        if (airTime.isNullOrEmpty()) {
            return null
        }

        return airTime.replaceFirst("(?i)^(monday|tuesday|wednesday|thursday|friday|saturday|sunday) ".toRegex(), "")
    }
}
