package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class ShowStatWrapper(@SerializedName("show.stats") val showStats: Map<Int, ShowStats>? = null) {
}
