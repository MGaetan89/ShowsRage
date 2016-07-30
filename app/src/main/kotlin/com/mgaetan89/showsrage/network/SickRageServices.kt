package com.mgaetan89.showsrage.network

import com.mgaetan89.showsrage.model.ApiKey
import com.mgaetan89.showsrage.model.Episodes
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.Histories
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.Logs
import com.mgaetan89.showsrage.model.RootDirs
import com.mgaetan89.showsrage.model.Schedules
import com.mgaetan89.showsrage.model.SearchResults
import com.mgaetan89.showsrage.model.Seasons
import com.mgaetan89.showsrage.model.ShowStatsWrapper
import com.mgaetan89.showsrage.model.Shows
import com.mgaetan89.showsrage.model.ShowsStats
import com.mgaetan89.showsrage.model.SingleEpisode
import com.mgaetan89.showsrage.model.SingleShow
import com.mgaetan89.showsrage.model.UpdateResponseWrapper
import retrofit.Callback
import retrofit.http.GET
import retrofit.http.Query
import retrofit.http.QueryMap

interface SickRageServices {
    @GET("/{api_path}/{api_key}/?cmd=show.addnew")
    fun addNewShow(@Query("indexerid") indexerId: Int, @Query("archive") preferredQuality: String?, @Query("initial") allowedQuality: String?, @Query("status") status: String?, @Query("lang") language: String?, @Query("anime") anime: Int, @Query("subtitles") subtitles: Int, @Query("location") location: String?, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=sb.checkversion")
    fun checkForUpdate(callback: Callback<UpdateResponseWrapper>)

    @GET("/{api_path}/{api_key}/?cmd=history.clear")
    fun clearHistory(callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=show.delete")
    fun deleteShow(@Query("indexerid") indexerId: Int, @Query("removefiles") removeFiles: Int, callback: Callback<GenericResponse>)

    @GET("/{web_root}getkey")
    fun getApiKey(@Query("u") username: String?, @Query("p") password: String?, callback: Callback<ApiKey>)

    @GET("/{api_path}/{api_key}/?cmd=episode&full_path=1")
    fun getEpisode(@Query("indexerid") indexerId: Int, @Query("season") season: Int, @Query("episode") episode: Int, callback: Callback<SingleEpisode>)

    @GET("/{api_path}/{api_key}/?cmd=show.seasons")
    fun getEpisodes(@Query("indexerid") indexerId: Int?, @Query("season") season: Int, callback: Callback<Episodes>)

    @GET("/{api_path}/{api_key}/?cmd=history")
    fun getHistory(callback: Callback<Histories>)

    @GET("/{api_path}/{api_key}/?cmd=history")
    fun getHistory(): Histories

    @GET("/{api_path}/{api_key}/?cmd=logs")
    fun getLogs(@Query("min_level") minLevel: LogLevel, callback: Callback<Logs>)

    @GET("/{api_path}/{api_key}/?cmd=sb.getrootdirs")
    fun getRootDirs(callback: Callback<RootDirs>)

    @GET("/{api_path}/{api_key}/?cmd=future")
    fun getSchedule(callback: Callback<Schedules>)

    @GET("/{api_path}/{api_key}/?cmd=show.seasonlist")
    fun getSeasons(@Query("indexerid") indexerId: Int, @Query("sort") sort: String, callback: Callback<Seasons>)

    @GET("/{api_path}/{api_key}/?cmd=show")
    fun getShow(@Query("indexerid") indexerId: Int, callback: Callback<SingleShow>)

    @GET("/{api_path}/{api_key}/?cmd=shows&sort=name")
    fun getShows(callback: Callback<Shows>)

    @GET("/{api_path}/{api_key}/?cmd=shows.stats")
    fun getShowsStats(callback: Callback<ShowsStats>)

    @GET("/{api_path}/{api_key}/")
    fun getShowStats(@Query("cmd") commands: String, @QueryMap parameters: Map<String, Int>, callback: Callback<ShowStatsWrapper>)

    @GET("/{api_path}/{api_key}/?cmd=sb.ping")
    fun ping(callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=show.pause")
    fun pauseShow(@Query("indexerid") indexerId: Int, @Query("pause") pause: Int, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=postprocess&type=manual")
    fun postProcess(@Query("is_priority") replace: Int, @Query("force_replace") forceProcessing: Int, @Query("process_method") processingMethod: String?, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=show.refresh")
    fun rescanShow(@Query("indexerid") indexerId: Int, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=sb.restart")
    fun restart(callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=sb.searchindexers")
    fun search(@Query("name") name: String, callback: Callback<SearchResults>)

    @GET("/{api_path}/{api_key}/?cmd=episode.search")
    fun searchEpisode(@Query("indexerid") indexerId: Int, @Query("season") season: Int, @Query("episode") episode: Int, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=episode.subtitlesearch")
    fun searchSubtitles(@Query("indexerid") indexerId: Int, @Query("season") season: Int, @Query("episode") episode: Int, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=episode.setstatus")
    fun setEpisodeStatus(@Query("indexerid") indexerId: Int, @Query("season") season: Int, @Query("episode") episode: Int, @Query("force") force: Int, @Query("status") status: String, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=show.setquality")
    fun setShowQuality(@Query("indexerid") indexerId: Int, @Query("initial") allowed: String?, @Query("archive") preferred: String?, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=show.update")
    fun updateShow(@Query("indexerid") indexerId: Int, callback: Callback<GenericResponse>)

    @GET("/{api_path}/{api_key}/?cmd=sb.update")
    fun updateSickRage(callback: Callback<GenericResponse>)
}
