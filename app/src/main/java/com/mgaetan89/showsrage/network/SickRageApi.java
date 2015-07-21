package com.mgaetan89.showsrage.network;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mgaetan89.showsrage.BuildConfig;
import com.mgaetan89.showsrage.model.Indexer;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public final class SickRageApi implements RequestInterceptor {
	private String apiKey = "";

	@NonNull
	private String apiUrl = "";

	@NonNull
	private static final SickRageApi INSTANCE = new SickRageApi();

	private String path = "";

	private SickRageServices services = null;

	private SickRageApi() {
	}

	@NonNull
	public static SickRageApi getInstance() {
		return INSTANCE;
	}

	@NonNull
	public String getApiUrl() {
		return buildFullApiUrl(this.apiUrl, this.path, this.apiKey);
	}

	@NonNull
	public String getFanArtUrl(int indexerId, @Nullable Indexer indexer) {
		if (indexer == null) {
			return this.getApiUrl() + "?cmd=show.getfanart";
		}

		return String.format("%s?cmd=show.getfanart&%s=%d", this.getApiUrl(), indexer.getParamName(), indexerId);
	}

	@NonNull
	public String getPosterUrl(int indexerId, @Nullable Indexer indexer) {
		if (indexer == null) {
			return this.getApiUrl() + "?cmd=show.getposter";
		}

		return String.format("%s?cmd=show.getposter&%s=%d", this.getApiUrl(), indexer.getParamName(), indexerId);
	}

	public SickRageServices getServices() {
		return this.services;
	}

	@NonNull
	public String getVideosUrl() {
		return this.apiUrl + "videos/";
	}

	public void init(@NonNull SharedPreferences preferences) {
		boolean useHttps = preferences.getBoolean("use_https", false);
		String address = preferences.getString("server_address", "");
		String portNumber = preferences.getString("server_port_number", "");

		this.path = preferences.getString("server_path", "");
		this.apiKey = preferences.getString("api_key", "");

		this.apiUrl = buildApiUrl(useHttps, address, portNumber);

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

	@NonNull
	private static String buildApiUrl(boolean useHttps, String address, String portNumber) {
		// Retrofit requires a non-empty endpoint
		// So we use the local url
		if (address.isEmpty()) {
			return "http://127.0.0.1/";
		}

		StringBuilder builder = new StringBuilder();

		if (useHttps) {
			builder.append("https://");
		} else {
			builder.append("http://");
		}

		builder.append(address);

		if (!portNumber.isEmpty()) {
			builder.append(":").append(portNumber);
		}

		builder.append("/");

		return builder.toString();
	}

	@NonNull
	private static String buildFullApiUrl(String apiUrl, String path, String apiKey) {
		String cleanPath = path.replaceAll("^/*|/*$", "");
		StringBuilder builder = new StringBuilder();
		builder.append(apiUrl);

		if (!cleanPath.isEmpty()) {
			builder.append(cleanPath).append("/");
		}

		if (!apiKey.isEmpty()) {
			builder.append(apiKey).append("/");
		}

		return builder.toString();
	}
}
