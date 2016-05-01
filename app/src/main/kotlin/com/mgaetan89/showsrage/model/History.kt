package com.mgaetan89.showsrage.model

import android.support.annotation.StringRes
import com.google.gson.annotations.SerializedName
import com.mgaetan89.showsrage.R
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class History : RealmObject() {
    open var date: String? = null
    open var episode: Int = 0
    @SerializedName("indexerid")
    open var indexerId: Int = 0
    open var provider: String? = null
    open var quality: String? = null
    @PrimaryKey
    open var resource: String? = null
    @SerializedName("resource_path")
    open var resourcePath: String? = null
    open var season: Int = 0
    @SerializedName("show_name")
    open var showName: String? = null
    open var status: String? = null
    @SerializedName("tvdbid")
    open var tvDbId: Int = 0
    open var version: Int = 0

    @StringRes
    fun getStatusTranslationResource(): Int {
        val normalizedStatus = this.status?.toLowerCase()

        return when (normalizedStatus) {
            "downloaded" -> return R.string.downloaded
            "snatched" -> return R.string.snatched
            else -> 0
        }
    }
}
