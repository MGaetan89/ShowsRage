package com.mgaetan89.showsrage.db_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Schedule extends RealmObject {
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

	public void setAirDate(String airDate) {
		this.airDate = airDate;
	}

	public void setAirs(String airs) {
		this.airs = airs;
	}

	public void setEpisode(int episode) {
		this.episode = episode;
	}

	public void setEpisodeName(String episodeName) {
		this.episodeName = episodeName;
	}

	public void setEpisodePlot(String episodePlot) {
		this.episodePlot = episodePlot;
	}

	public void setIndexerId(int indexerId) {
		this.indexerId = indexerId;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public void setPaused(int paused) {
		this.paused = paused;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public void setTvDbId(int tvDbId) {
		this.tvDbId = tvDbId;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
}
