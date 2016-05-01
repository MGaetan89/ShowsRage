package com.mgaetan89.showsrage.model

import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.mgaetan89.showsrage.R
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Episode : RealmObject() {
    @SerializedName("airdate")
    open var airDate: String = ""
    open var description: String? = ""
    @SerializedName("file_size")
    open var fileSize: Long = 0L
    @SerializedName("file_size_human")
    open var fileSizeHuman: String? = ""
    @PrimaryKey
    open var id: String = ""
    open var indexerId: Int = 0
    open var location: String? = ""
    open var name: String = ""
    open var number: Int = 0
    open var quality: String = ""
    @SerializedName("release_name")
    open var releaseName: String? = ""
    open var season: Int = 0
    open var status: String? = ""
    open var subtitles: String = ""

    @ColorRes
    fun getStatusBackgroundColor(): Int {
        val normalizedStatus = this.status?.toLowerCase()

        return when (normalizedStatus) {
            "archived", "downloaded" -> R.color.green
            "ignored", "skipped" -> R.color.blue
            "snatched", "snatched (proper)" -> R.color.purple
            "unaired" -> R.color.yellow
            "wanted" -> R.color.red
            else -> android.R.color.transparent
        }
    }

    @StringRes
    fun getStatusTranslationResource(): Int {
        val normalizedStatus = this.status?.toLowerCase()

        return when (normalizedStatus) {
            "archived" -> R.string.archived
            "downloaded" -> R.string.downloaded
            "ignored" -> R.string.ignored
            "skipped" -> R.string.skipped
            "snatched" -> R.string.snatched
            "snatched (proper)" -> R.string.snatched_proper
            "unaired" -> R.string.unaired
            "wanted" -> R.string.wanted
            else -> 0
        }
    }

    companion object {
        fun buildId(indexer: Int, season: Int, episode: Int) = "${indexer}_${season}_$episode"

        fun getStatusForMenuId(@IdRes menuId: Int?): String? {
            return when (menuId) {
                R.id.menu_episode_set_status_failed -> "failed"
                R.id.menu_episode_set_status_ignored -> "ignored"
                R.id.menu_episode_set_status_skipped -> "skipped"
                R.id.menu_episode_set_status_wanted -> "wanted"
                else -> null
            }
        }
    }
}
