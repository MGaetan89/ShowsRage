package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

public class History {
	private String date;
	private int episode;
	@SerializedName("indexerid")
	private int indexerId;
	private String provider;
	private String quality;
	private String resource;
	@SerializedName("resource_path")
	private String resourcePath;
	private int season;
	@SerializedName("show_name")
	private String showName;
	private String status;
	@SerializedName("tvdbid")
	private int tvDbId;
	private int version;

	public String getDate() {
		return this.date;
	}

	public int getEpisode() {
		return this.episode;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public String getProvider() {
		return this.provider;
	}

	public String getQuality() {
		return this.quality;
	}

	public String getResource() {
		return this.resource;
	}

	public String getResourcePath() {
		return this.resourcePath;
	}

	public int getSeason() {
		return this.season;
	}

	public String getShowName() {
		return this.showName;
	}

	public String getStatus() {
		return this.status;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public int getVersion() {
		return this.version;
	}
}
