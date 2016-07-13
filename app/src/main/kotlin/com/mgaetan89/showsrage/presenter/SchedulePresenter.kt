package com.mgaetan89.showsrage.presenter

import android.content.Context
import android.text.format.DateUtils
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.network.SickRageApi

open class SchedulePresenter(val schedule: Schedule?, val context: Context?) {
    fun getAirDate(): CharSequence? {
        val schedule = this._getSchedule() ?: return null
        val airDate = schedule.airDate

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
        val schedule = this._getSchedule() ?: return null

        if (this.context == null) {
            return null
        }

        val airDate = schedule.airDate
        val airTime = this.getAirTimeOnly()

        if (airDate.isNullOrEmpty() || airTime.isNullOrEmpty()) {
            return null
        }

        return DateTimeHelper.getLocalizedTime(this.context, "$airDate $airTime", "yyyy-MM-dd K:mm a")
    }

    fun getEpisode() = this._getSchedule()?.episode ?: 0

    fun getNetwork() = this._getSchedule()?.network ?: ""

    fun getPosterUrl(): String {
        val schedule = this._getSchedule() ?: return ""

        return SickRageApi.instance.getPosterUrl(schedule.tvDbId, Indexer.TVDB)
    }

    fun getQuality() = this._getSchedule()?.quality ?: ""

    fun getSeason() = this._getSchedule()?.season ?: 0

    fun getShowName() = this._getSchedule()?.showName ?: ""

    internal fun getAirTimeOnly(): String? {
        val schedule = this._getSchedule() ?: return null
        val airTime = schedule.airs

        if (airTime.isNullOrEmpty()) {
            return null
        }

        return airTime.replaceFirst("(?i)^(monday|tuesday|wednesday|thursday|friday|saturday|sunday) ".toRegex(), "")
    }

    internal fun _getSchedule() = if (this.schedule?.isValid ?: false) this.schedule else null
}
