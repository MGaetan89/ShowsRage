package com.mgaetan89.showsrage.db_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Episode extends RealmObject {
	@SerializedName("airdate")
	private String airDate = "";
	private String description = "";
	@SerializedName("file_size")
	private long fileSize = 0L;
	@SerializedName("file_size_human")
	private String fileSizeHuman = "";
	private String location = "";
	private String name = "";
	private String quality = "";
	@SerializedName("release_name")
	private String releaseName = "";
	private String status = "";
	private String subtitles = "";

	public String getAirDate() {
		return this.airDate;
	}

	public String getDescription() {
		return this.description;
	}

	public long getFileSize() {
		return this.fileSize;
	}

	public String getFileSizeHuman() {
		return this.fileSizeHuman;
	}

	public String getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name;
	}

	public String getQuality() {
		return this.quality;
	}

	public String getReleaseName() {
		return this.releaseName;
	}

	public String getStatus() {
		return this.status;
	}

	public String getSubtitles() {
		return this.subtitles;
	}

	public void setAirDate(String airDate) {
		this.airDate = airDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void setFileSizeHuman(String fileSizeHuman) {
		this.fileSizeHuman = fileSizeHuman;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}
}
