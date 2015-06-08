package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ComingEpisode implements Serializable {
	private static final long serialVersionUID = -4883158127180444453L;

	@SerializedName("airdate")
	private String airDate;
	private String airs;
	private int episode;
	@SerializedName("ep_name")
	private String episodeName;
	@SerializedName("ep_plot")
	private String episodePlot;
	@SerializedName("indexerid")
	private int indexerId;
	private String network;
	private int paused;
	private String quality;
	private int season;
	@SerializedName("show_name")
	private String showName;
	@SerializedName("show_status")
	private String showStatus;
	@SerializedName("tvdbid")
	private int tvDbId;
	private int weekday;

	public String getAirDate() {
		return this.airDate;
	}

	public String getAirs() {
		return this.airs;
	}

	public int getEpisode() {
		return this.episode;
	}

	public String getEpisodeName() {
		return this.episodeName;
	}

	public String getEpisodePlot() {
		return this.episodePlot;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public String getNetwork() {
		return this.network;
	}

	public int getPaused() {
		return this.paused;
	}

	public String getQuality() {
		return this.quality;
	}

	public int getSeason() {
		return this.season;
	}

	public String getShowName() {
		return this.showName;
	}

	public String getShowStatus() {
		return this.showStatus;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public int getWeekday() {
		return this.weekday;
	}
}
