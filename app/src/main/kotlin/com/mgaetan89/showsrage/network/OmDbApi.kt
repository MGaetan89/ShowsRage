package com.mgaetan89.showsrage.network

import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.Serie

import retrofit.Callback
import retrofit.http.GET
import retrofit.http.Query

interface OmDbApi {
	@GET("/?plot=full")
	fun getEpisodeByImDbId(@Query("i") imDbId: String, @Query("Season") season: Int, @Query("Episode") episode: Int, callback: Callback<OmDbEpisode>)

	@GET("/?plot=full")
	fun getEpisodeByTitle(@Query("t") title: String, @Query("Season") season: Int, @Query("Episode") episode: Int, callback: Callback<OmDbEpisode>)

	@GET("/?plot=full")
	fun getShow(@Query("i") imDbId: String, callback: Callback<Serie>)
}
