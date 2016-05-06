package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class OmDbEpisode : RealmObject() {
    @SerializedName("Actors")
    open var actors: String? = null
    @SerializedName("Awards")
    open var awards: String? = null
    @SerializedName("Country")
    open var country: String? = null
    @SerializedName("Director")
    open var director: String? = null
    @SerializedName("Episode")
    open var episode: String? = null
    @SerializedName("Genre")
    open var genre: String? = null
    @PrimaryKey
    open var id: String = ""
    @SerializedName("imdbID")
    open var imdbId: String? = null
    open var imdbRating: String? = null
    open var imdbVotes: String? = null
    @SerializedName("Language")
    open var language: String? = null
    @SerializedName("Metascore")
    open var metascore: String? = null
    @SerializedName("Plot")
    open var plot: String? = null
    @SerializedName("Poster")
    open var poster: String? = null
    @SerializedName("Rated")
    open var rated: String? = null
    @SerializedName("Released")
    open var released: String? = null
    @SerializedName("Response")
    open var response: String? = null
    @SerializedName("Runtime")
    open var runtime: String? = null
    @SerializedName("Season")
    open var season: String? = null
    @SerializedName("seriesID")
    open var seriesId: String? = null
    @SerializedName("Title")
    open var title: String? = null
    @SerializedName("Type")
    open var type: String? = null
    @SerializedName("Writer")
    open var writer: String? = null
    @SerializedName("Year")
    open var year: String? = null

    companion object {
        fun buildId(imdbId: String, season: String, episode: String): String {
            return "${imdbId}_${season}_$episode"
        }
    }
}
