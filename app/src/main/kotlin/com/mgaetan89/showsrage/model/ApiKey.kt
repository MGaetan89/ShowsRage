package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class ApiKey(@SerializedName("api_key") val apiKey: String = "", val success: Boolean = false) {
}
