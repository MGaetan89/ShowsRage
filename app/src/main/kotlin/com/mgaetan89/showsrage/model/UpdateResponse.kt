package com.mgaetan89.showsrage.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class UpdateResponse(
		@SerializedName("commits_offset") val commitsOffset: Int = 0,
		@SerializedName("current_version") val currentVersion: Version? = null,
		@SerializedName("latest_version") val latestVersion: Version? = null,
		@SerializedName("needs_update") val needsUpdate: Boolean = false
) : Parcelable
