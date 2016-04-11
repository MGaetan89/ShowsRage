package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class ShowsStat(
        @SerializedName("ep_downloaded") val episodesDownloaded: Int = 0,
        @SerializedName("ep_snatched") val episodesSnatched: Int = 0,
        @SerializedName("ep_total") val episodesTotal: Int = 0,
        @SerializedName("shows_active") val showsActive: Int = 0,
        @SerializedName("shows_total") val showsTotal: Int = 0
) {
    val episodesMissing: Int
        get() = this.episodesTotal - (this.episodesDownloaded + this.episodesSnatched)
}
