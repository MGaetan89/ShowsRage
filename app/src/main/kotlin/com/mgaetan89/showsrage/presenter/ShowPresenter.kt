package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi

open class ShowPresenter(val show: Show?) {
    fun getBannerUrl() = if (this.isShowValid()) SickRageApi.instance.getBannerUrl(this.show!!.tvDbId, Indexer.TVDB) else ""

    fun getDownloaded() = this.getShowStat()?.downloaded ?: 0

    fun getEpisodesCount() = this.getShowStat()?.episodesCount ?: 0

    fun getNetwork() = if (this.isShowValid()) this.show!!.network else ""

    fun getPosterUrl() = if (this.isShowValid()) SickRageApi.instance.getPosterUrl(this.show!!.tvDbId, Indexer.TVDB) else ""

    fun getQuality() = if (this.isShowValid()) this.show!!.quality else ""

    fun getShowName() = if (this.isShowValid()) this.show!!.showName else ""

    fun getSnatched() = this.getShowStat()?.snatched ?: 0

    fun isPaused() = if (this.isShowValid()) this.show!!.paused == 1 else false

    private fun getShowStat(): RealmShowStat? {
        if (!this.isShowValid()) {
            return null
        }

        if (this.show!!.stat == null) {
            this.show.stat = RealmManager.getShowStat(this.show.indexerId)
        }

        return this.show.stat
    }

    internal fun isShowValid() = this.show != null && this.show.isValid
}
