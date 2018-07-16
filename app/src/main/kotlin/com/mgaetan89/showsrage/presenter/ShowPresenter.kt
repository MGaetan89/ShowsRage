package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.extension.getShowStat
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm

class ShowPresenter(val show: Show?) {
	fun getBannerUrl() = if (this.isShowValid()) SickRageApi.instance.getBannerUrl(this.show!!.tvDbId, Indexer.TVDB) else ""

	fun getNetwork() = if (this.isShowValid()) this.show!!.network else ""

	fun getPosterUrl() = if (this.isShowValid()) SickRageApi.instance.getPosterUrl(this.show!!.tvDbId, Indexer.TVDB) else ""

	fun getQuality() = if (this.isShowValid()) this.show!!.quality else ""

	fun getShowName() = if (this.isShowValid()) this.show!!.showName else ""

	fun getShowStat(): RealmShowStat? {
		if (!this.isShowValid()) {
			return null
		}

		if (this.show!!.stat == null) {
			this.show.stat = Realm.getDefaultInstance().use {
				it.getShowStat(this.show.indexerId)
			}
		}

		return this.show.stat
	}

	fun isPaused() = if (this.isShowValid()) this.show!!.paused == 1 else false

	internal fun isShowValid() = this.show != null && this.show.isValid
}
