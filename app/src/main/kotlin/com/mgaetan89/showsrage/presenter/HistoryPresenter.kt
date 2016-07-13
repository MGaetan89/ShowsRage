package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.network.SickRageApi

open class HistoryPresenter(val history: History?) {
    fun getEpisode() = if (this.isHistoryValid()) this.history!!.episode else 0

    fun getPosterUrl() = if (this.isHistoryValid()) SickRageApi.instance.getPosterUrl(this.history!!.tvDbId, Indexer.TVDB) else ""

    fun getProvider() = if (this.isHistoryValid()) this.history!!.provider else ""

    fun getProviderQuality(): String? {
        return if (this.isHistoryValid() && "-1".equals(this.history!!.provider)) this.history.quality else null
    }

    fun getQuality() = if (this.isHistoryValid()) this.history!!.quality else ""

    fun getSeason() = if (this.isHistoryValid()) this.history!!.season else 0

    fun getShowName() = if (this.isHistoryValid()) this.history!!.showName else ""

    internal fun isHistoryValid() = this.history != null && this.history.isValid
}
