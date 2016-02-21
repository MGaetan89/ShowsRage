package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi

class ShowPresenter(val show: Show?) {
    fun getBannerUrl() = if (this.show == null) "" else SickRageApi.instance.getBannerUrl(this.show.tvDbId, Indexer.TVDB)

    fun getDownloaded() = this.show?.downloaded ?: 0

    fun getEpisodesCount() = this.show?.episodesCount ?: 0

    fun getNetwork() = this.show?.network ?: ""

    fun getPosterUrl() = if (this.show == null) "" else SickRageApi.instance.getPosterUrl(this.show.tvDbId, Indexer.TVDB)

    fun getQuality() = this.show?.quality ?: ""

    fun getShowName() = this.show?.showName ?: ""

    fun getSnatched() = this.show?.snatched ?: 0

    fun isPaused() = if (this.show == null) false else this.show.paused == 1
}
