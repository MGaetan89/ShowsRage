package com.mgaetan89.showsrage.db_model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Show extends RealmObject {
	private String airs = "";
	private int anime = 0;
	private int downloaded = 0;
	private int episodesCount = 0;
	private RealmList<RealmString> genre = null; // TODO This should be RealmList<String>
	@SerializedName("imdbid")
	private String imdbId = "";
	@PrimaryKey
	@SerializedName("indexerid")
	private int indexerId = 0;
	private String language = "";
	private String location = "";
	private String network = "";
	@SerializedName("next_ep_airdate")
	private String nextEpisodeAirDate = "";
	private int paused = 0;
	private String quality = "";
	@SerializedName("quality_details")
	private Quality qualityDetails = null;
	@SerializedName("show_name")
	private String showName = "";
	private int snatched = 0;
	private String status = "";
	@SerializedName("tvdbid")
	private int tvDbId = 0;

	public String getAirs() {
		return this.airs;
	}

	public int getAnime() {
		return this.anime;
	}

	public int getDownloaded() {
		return this.downloaded;
	}

	public int getEpisodesCount() {
		return this.episodesCount;
	}

	public RealmList<RealmString> getGenre() {
		return this.genre;
	}

	public String getImdbId() {
		return this.imdbId;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getLocation() {
		return this.location;
	}

	public String getNetwork() {
		return this.network;
	}

	public String getNextEpisodeAirDate() {
		return this.nextEpisodeAirDate;
	}

	public int getPaused() {
		return this.paused;
	}

	public String getQuality() {
		return this.quality;
	}

	public Quality getQualityDetails() {
		return this.qualityDetails;
	}

	public String getShowName() {
		return this.showName;
	}

	public int getSnatched() {
		return this.snatched;
	}

	public String getStatus() {
		return this.status;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public void setAirs(String airs) {
		this.airs = airs;
	}

	public void setAnime(int anime) {
		this.anime = anime;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	public void setEpisodesCount(int episodesCount) {
		this.episodesCount = episodesCount;
	}

	public void setGenre(RealmList<RealmString> genre) {
		this.genre = genre;
	}

	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}

	public void setIndexerId(int indexerId) {
		this.indexerId = indexerId;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public void setNextEpisodeAirDate(String nextEpisodeAirDate) {
		this.nextEpisodeAirDate = nextEpisodeAirDate;
	}

	public void setPaused(int paused) {
		this.paused = paused;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setQualityDetails(Quality qualityDetails) {
		this.qualityDetails = qualityDetails;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public void setSnatched(int snatched) {
		this.snatched = snatched;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTvDbId(int tvDbId) {
		this.tvDbId = tvDbId;
	}
}
