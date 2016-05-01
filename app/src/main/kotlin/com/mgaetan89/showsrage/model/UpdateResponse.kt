package com.mgaetan89.showsrage.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UpdateResponse(
        @SerializedName("commits_offset") val commitsOffset: Int = 0,
        @SerializedName("current_version") val currentVersion: Version? = null,
        @SerializedName("latest_version") val latestVersion: Version? = null,
        @SerializedName("needs_update") val needsUpdate: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readValue(Version::class.java.classLoader) as Version?,
            parcel.readValue(Version::class.java.classLoader) as Version?,
            parcel.readByte().toInt() != 0
    ) {
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(this.commitsOffset)
        dest.writeValue(this.currentVersion)
        dest.writeValue(this.latestVersion)
        dest.writeByte(if (this.needsUpdate) 1.toByte() else 0.toByte())
    }

    val CREATOR: Parcelable.Creator<UpdateResponse> = object : Parcelable.Creator<UpdateResponse> {
        override fun createFromParcel(`in`: Parcel): UpdateResponse {
            return UpdateResponse(`in`)
        }

        override fun newArray(size: Int): Array<UpdateResponse> {
            return newArray(size)
        }
    }
}
