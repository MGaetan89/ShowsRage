package com.mgaetan89.showsrage.network;

import com.mgaetan89.showsrage.model.ComingEpisodes;
import com.mgaetan89.showsrage.model.Episodes;
import com.mgaetan89.showsrage.model.LogLevel;
import com.mgaetan89.showsrage.model.Logs;
import com.mgaetan89.showsrage.model.Seasons;
import com.mgaetan89.showsrage.model.Shows;
import com.mgaetan89.showsrage.model.SingleShow;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface SickRageServices {
	@GET("/?cmd=future")
	void getComingEpisodes(Callback<ComingEpisodes> callback);

	@GET("/?cmd=episode")
	void getEpisode(@Query("indexerid") int indexerId, @Query("season") int season, @Query("episode") int episode, Callback<Object> callback);

	@GET("/?cmd=show.seasons")
	void getEpisodes(@Query("indexerid") int indexerId, @Query("season") int season, Callback<Episodes> callback);

	@GET("/?cmd=logs")
	void getLogs(@Query("min_level") LogLevel minLevel, Callback<Logs> callback);

	@GET("/?cmd=show.seasonlist")
	void getSeasons(@Query("indexerid") int indexerId, Callback<Seasons> callback);

	@GET("/?cmd=show")
	void getShow(@Query("indexerid") int indexerId, Callback<SingleShow> callback);

	@GET("/?cmd=shows&sort=name")
	void getShows(Callback<Shows> callback);
}
