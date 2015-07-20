package com.mgaetan89.showsrage.network;

import com.mgaetan89.showsrage.model.Serie;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface OmDbApi {
	@GET("/?plot=full")
	void getShow(@Query("i") String imDbId, Callback<Serie> callback);
}
