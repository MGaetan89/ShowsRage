package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Episode implements Serializable {
	private static final long serialVersionUID = -7301513909240986546L;

	@SerializedName("airdate")
	private String airDate;

	private String description;

	@SerializedName("file_size")
	private long fileSize;

	@SerializedName("file_size_human")
	private String fileSizeHuman;

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
}
