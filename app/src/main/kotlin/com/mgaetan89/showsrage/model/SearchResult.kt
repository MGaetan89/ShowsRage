package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class SearchResult(
        @SerializedName("langid") val langId: Int = 0,
        val results: List<SearchResultItem>? = null
) {
}
