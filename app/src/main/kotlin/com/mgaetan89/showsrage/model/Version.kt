package com.mgaetan89.showsrage.model

import android.os.Parcel
import android.os.Parcelable

class Version(val branch: String = "", val commit: String = "", val version: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(), parcel.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.branch)
        dest.writeString(this.commit)
        dest.writeString(this.version)
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Version> {
            override fun createFromParcel(`in`: Parcel): Version {
                return Version(`in`)
            }

            override fun newArray(size: Int): Array<Version?> {
                return arrayOfNulls(size)
            }
        }
    }
}
