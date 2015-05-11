package com.mgaetan89.showsrage.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.RestAdapter;

@Singleton
public class SickRageApi {
	@NonNull
	private String baseUrl = "";

	private SickRageServices services = null;

	@Inject
	public SickRageApi(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String protocol = preferences.getBoolean("use_https", false) ? "https" : "http";
		String address = preferences.getString("server_address", "");
		String portNumber = preferences.getString("server_port_number", "");
		String path = preferences.getString("server_path", "");
		String apiKey = preferences.getString("api_key", "");

		this.baseUrl = String.format("%s://%s:%s/%s/%s/", protocol, address, portNumber, path, apiKey);

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setEndpoint(this.baseUrl)
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.build();

		this.services = restAdapter.create(SickRageServices.class);
	}

	@NonNull
	public String getBaseUrl() {
		return this.baseUrl;
	}

	public SickRageServices getServices() {
		return this.services;
	}
}
