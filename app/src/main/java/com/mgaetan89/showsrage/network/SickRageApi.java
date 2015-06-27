package com.mgaetan89.showsrage.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.mgaetan89.showsrage.BuildConfig;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class SickRageApi implements RequestInterceptor {
	private String apiKey = "";

	@NonNull
	private String apiUrl = "";

	private static final SickRageApi INSTANCE = new SickRageApi();

	private String path = "";

	private SickRageServices services = null;

	@NonNull
	private String videosUrl = "";

	private SickRageApi() {
	}

	public static SickRageApi getInstance() {
		return INSTANCE;
	}

	@NonNull
	public String getApiUrl() {
		return this.apiUrl + this.path + "/" + this.apiKey + "/";
	}

	@NonNull
	public String getPosterUrl(int tvDbId) {
		return String.format("%s?cmd=show.getposter&tvdbid=%d", this.getApiUrl(), tvDbId);
	}

	public SickRageServices getServices() {
		return this.services;
	}

	@NonNull
	public String getVideosUrl() {
		return this.videosUrl;
	}

	public void init(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String protocol = preferences.getBoolean("use_https", false) ? "https" : "http";
		String address = preferences.getString("server_address", "");
		String portNumber = preferences.getString("server_port_number", "");

		this.path = preferences.getString("server_path", "");
		this.apiKey = preferences.getString("api_key", "");

		this.apiUrl = String.format("%s://%s:%s/", protocol, address, portNumber);
		this.videosUrl = String.format("%s://%s:%s/videos/", protocol, address, portNumber);

		RestAdapter.Builder builder = new RestAdapter.Builder();
		builder.setEndpoint(this.apiUrl);
		builder.setRequestInterceptor(this);

		if (BuildConfig.DEBUG) {
			builder.setLogLevel(RestAdapter.LogLevel.FULL);
		} else {
			builder.setLogLevel(RestAdapter.LogLevel.NONE);
		}

		this.services = builder.build().create(SickRageServices.class);
	}

	@Override
	public void intercept(RequestFacade request) {
		request.addPathParam("api_path", this.path);
		request.addPathParam("api_key", this.apiKey);
	}
}
