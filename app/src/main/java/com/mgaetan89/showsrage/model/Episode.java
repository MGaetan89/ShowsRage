package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Episode implements Serializable {
	@SerializedName("airdate")
	private String airDate;

	@SerializedName("file_size")
	private long fileSize;

	private String location;

	private String name;

	private String quality;

	@SerializedName("release_name")
	private String releaseName;

	private String status;

	private String subtitles;

	public String getAirDate() {
		return this.airDate;
	}

	public long getFileSize() {
		return this.fileSize;
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
}
