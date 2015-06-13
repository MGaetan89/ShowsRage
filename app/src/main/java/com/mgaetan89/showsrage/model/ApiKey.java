package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiKey implements Serializable {
	private static final long serialVersionUID = -2281295947978326582L;

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
