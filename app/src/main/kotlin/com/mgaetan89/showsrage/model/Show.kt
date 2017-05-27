package com.mgaetan89.showsrage.model

import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.mgaetan89.showsrage.R
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

open class Show : RealmObject() {
	@SerializedName("air_by_date")
	open var airByDate: Int = 0
	open var airs: String? = ""
	open var anime: Int = 0
	@SerializedName("archive_firstmatch")
	open var archiveFirstmatch: Int = 0
	@SerializedName("dvdorder")
	open var dvdOrder: Int = 0
	@SerializedName("flatten_folders")
	open var flattenFolders: Int = 0
	open var genre: RealmList<RealmString>? = null
	@SerializedName("imdbid")
	open var imdbId: String? = ""
	@PrimaryKey
	@SerializedName("indexerid")
	open var indexerId: Int = 0
	open var language: String? = ""
	open var location: String? = ""
	open var network: String = ""
	@SerializedName("next_ep_airdate")
	open var nextEpisodeAirDate: String = ""
	open var paused: Int = 0
	open var quality: String = ""
	@SerializedName("quality_details")
	open var qualityDetails: Quality? = null
	open var scene: Int = 0
	@SerializedName("season_list")
	open var seasonList: RealmList<RealmString>? = null
	@SerializedName("show_name")
	open var showName: String? = ""
	open var sports: Int = 0
	@Ignore
	open var stat: RealmShowStat? = null
	open var status: String? = ""
	open var subtitles: Int = 0
	@SerializedName("tvdbid")
	open var tvDbId: Int = 0
	@SerializedName("tvrage_id")
	open var tvRageId: Int = 0
	@SerializedName("tvrage_name")
	open var tvRageName: String = ""

	fun getSeasonsListInt(): List<Int> {
		return this.seasonList?.map {
			try {
				it.value.toInt()
			} catch(exception: NumberFormatException) {
				-1
			}
		}?.filter { it >= 0 } ?: emptyList()
	}

	@StringRes
	fun getStatusTranslationResource(): Int {
		val normalizedStatus = this.status?.toLowerCase()

		return when (normalizedStatus) {
			"continuing" -> R.string.continuing
			"ended" -> R.string.ended
			"unknown" -> R.string.unknown
			else -> 0
		}
	}
}
