package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

public class ApiKey {
	@SerializedName("api_key")
	private String apiKey;

	private boolean success;

	public String getApiKey() {
		return this.apiKey;
	}

	public boolean isSuccess() {
		return this.success;
	}
}
