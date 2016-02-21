package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.network.SickRageApi

class HistoryPresenter(val history: History?) {
    fun getEpisode() = this.history?.episode ?: 0

    fun getPosterUrl() = if (this.history == null) "" else SickRageApi.instance.getPosterUrl(this.history.tvDbId, Indexer.TVDB)

    fun getProvider() = this.history?.provider ?: ""

    fun getProviderQuality(): String? {
        if (this.history == null) {
            return null
        }

        return if ("-1".equals(this.history.provider)) this.history.quality else null
    }

    fun getQuality() = this.history?.quality ?: ""

    fun getSeason() = this.history?.season ?: 0

    fun getShowName() = this.history?.showName ?: ""
}
