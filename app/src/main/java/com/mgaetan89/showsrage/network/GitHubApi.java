package com.mgaetan89.showsrage.network;

import com.mgaetan89.showsrage.model.MarkdownParseBody;
import com.mgaetan89.showsrage.model.ReleasesResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

public interface GitHubApi {
	@GET("/repos/MGaetan89/ShowsRage/releases")
	@Headers("Accept: application/vnd.github.v3+json")
	void getReleases(Callback<ReleasesResponse> callback);

	@Headers("Accept: application/vnd.github.v3+json")
	@POST("/markdown")
	void parseMarkdown(@Body MarkdownParseBody body, Callback<Response> callback);
}
