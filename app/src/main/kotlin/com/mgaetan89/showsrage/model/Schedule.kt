package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey

open class Schedule : RealmObject() {
	@SerializedName("airdate")
	open var airDate: String = ""
	open var airs: String = ""
	open var episode: Int = 0
	@SerializedName("ep_name")
	open var episodeName: String = ""
	@SerializedName("ep_plot")
	open var episodePlot: String? = ""
	@PrimaryKey
	open var id: String = ""
	@SerializedName("indexerid")
	open var indexerId: Int = 0
	open var network: String = ""
	open var paused: Int = 0
	open var quality: String = ""
	@Index
	open var section: String = ""
	open var season: Int = 0
	@SerializedName("show_name")
	open var showName: String = ""
	@SerializedName("show_status")
	open var showStatus: String? = null
	@SerializedName("tvdbid")
	open var tvDbId: Int = 0
	open var weekday: Int = 0
}
