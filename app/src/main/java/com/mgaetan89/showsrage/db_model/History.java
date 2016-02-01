package com.mgaetan89.showsrage.db_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class History extends RealmObject {
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

	public void setDate(String date) {
		this.date = date;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public void setIndexerId(int indexerId) {
		this.indexerId = indexerId;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTvDbId(int tvDbId) {
		this.tvDbId = tvDbId;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
