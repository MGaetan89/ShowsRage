package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class Serie(
        @SerializedName("Actors") val actors: String? = null,
        @SerializedName("Awards") val awards: String? = null,
        @SerializedName("Country") val country: String? = null,
        @SerializedName("Director") val director: String? = null,
        @SerializedName("Genre") val genre: String? = null,
        @SerializedName("imdbID") val imdbId: String? = null,
        val imdbRating: String? = null,
        val imdbVotes: String? = null,
        @SerializedName("Language") val language: String? = null,
        @SerializedName("Metascore") val metascore: String? = null,
        @SerializedName("Plot") val plot: String? = null,
        @SerializedName("Poster") val poster: String? = null,
        @SerializedName("Rated") val rated: String? = null,
        @SerializedName("Released") val released: String? = null,
        @SerializedName("Response") val response: String? = null,
        @SerializedName("Runtime") val runtime: String? = null,
        @SerializedName("Title") val title: String? = null,
        @SerializedName("Type") val type: String? = null,
        @SerializedName("Writer") val writer: String? = null,
        @SerializedName("Year") val year: String? = null
) {
}
