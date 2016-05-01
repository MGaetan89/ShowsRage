package com.mgaetan89.showsrage.model

import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.mgaetan89.showsrage.R

data class SearchResultItem(
        @SerializedName("first_aired") val firstAired: String? = null,
        val indexer: Int = 0,
        val name: String? = null,
        @SerializedName("tvdbid") val tvDbId: Int = 0,
        @SerializedName("tvrageid") val tvRageId: Int = 0
) {
    fun getIndexerId(): Int {
        return when (this.indexer) {
            1 -> this.tvDbId
            2 -> this.tvRageId
            else -> 0
        }
    }

    @StringRes
    fun getIndexerNameResource(): Int {
        return when (this.indexer) {
            1 -> R.string.the_tvdb
            2 -> R.string.tvrage
            else -> 0
        }
    }

    fun getIndexerType(): Indexer? {
        return when (this.indexer) {
            1 -> Indexer.TVDB
            2 -> Indexer.TVRAGE
            else -> null
        }
    }
}
