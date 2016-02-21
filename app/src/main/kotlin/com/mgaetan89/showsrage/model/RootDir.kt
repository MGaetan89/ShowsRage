package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class RootDir(
        @SerializedName("default") val defaultDir: Int = 0,
        val location: String = "",
        val valid: Int = 0
) {
}
