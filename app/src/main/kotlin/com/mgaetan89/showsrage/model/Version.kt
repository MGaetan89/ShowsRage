package com.mgaetan89.showsrage.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Version(private val branch: String = "", private val commit: String = "", val version: String = "") : Parcelable
