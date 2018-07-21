package com.mgaetan89.showsrage.model

import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.mgaetan89.showsrage.R

data class SearchResultItem(
		@SerializedName("first_aired") val firstAired: String? = null,
		val indexer: Int = 0,
		val name: String? = null,
		@SerializedName("tmdbid") val tmDbId: Int = 0,
		@SerializedName("tvdbid") val tvDbId: Int = 0,
		@SerializedName("tvmazeid") val tvMazeId: Int = 0,
		@SerializedName("tvrageid") val tvRageId: Int = 0
) {
	fun getIndexerId() = when (this.indexer) {
		1 -> this.tvDbId
		2 -> this.tvRageId
		3 -> this.tvMazeId
		4 -> this.tmDbId
		else -> 0
	}

	@StringRes
	fun getIndexerNameResource() = when (this.indexer) {
		1 -> R.string.tvdb
		2 -> R.string.tvrage
		3 -> R.string.tvmaze
		4 -> R.string.tmdb
		else -> R.string.unknown
	}
}
