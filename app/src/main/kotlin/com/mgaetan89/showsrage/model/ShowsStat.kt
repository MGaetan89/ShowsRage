package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

open class ShowsStat : RealmObject() {
	@SerializedName("ep_downloaded")
	open var episodesDownloaded: Int = 0
	@SerializedName("ep_snatched")
	open var episodesSnatched: Int = 0
	@SerializedName("ep_total")
	open var episodesTotal: Int = 0
	@SerializedName("shows_active")
	open var showsActive: Int = 0
	@SerializedName("shows_total")
	open var showsTotal: Int = 0

	val episodesMissing: Int
		get() = this.episodesTotal - (this.episodesDownloaded + this.episodesSnatched)
}
