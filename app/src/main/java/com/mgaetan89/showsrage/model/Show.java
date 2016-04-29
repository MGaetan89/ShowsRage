package com.mgaetan89.showsrage.model;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.mgaetan89.showsrage.R;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Show extends RealmObject {
	@SerializedName("air_by_date")
	private int airByDate = 0;
	private String airs = "";
	private int anime = 0;
	@SerializedName("archive_firstmatch")
	private int archiveFirstmatch = 0;
	private int downloaded = 0;
	@SerializedName("dvdorder")
	private int dvdOrder = 0;
	private int episodesCount = 0;
	@SerializedName("flatten_folders")
	private int flattenFolders = 0;
	private RealmList<RealmString> genre = null;
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
	private int scene = 0;
	@SerializedName("season_list")
	private RealmList<RealmString> seasonList = null;
	@SerializedName("show_name")
	private String showName = "";
	private int snatched = 0;
	private int sports = 0;
	private String status = "";
	private int subtitles = 0;
	@SerializedName("tvdbid")
	private int tvDbId = 0;
	@SerializedName("tvrage_id")
	private int tvRageId = 0;
	@SerializedName("tvrage_name")
	private String tvRageName = "";

	public int getAirByDate() {
		return this.airByDate;
	}

	public String getAirs() {
		return this.airs;
	}

	public int getAnime() {
		return this.anime;
	}

	public int getArchiveFirstmatch() {
		return this.archiveFirstmatch;
	}

	public int getDownloaded() {
		return this.downloaded;
	}

	public int getDvdOrder() {
		return this.dvdOrder;
	}

	public int getEpisodesCount() {
		return this.episodesCount;
	}

	public int getFlattenFolders() {
		return this.flattenFolders;
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

	public int getScene() {
		return this.scene;
	}

	public RealmList<RealmString> getSeasonList() {
		return this.seasonList;
	}

	public String getShowName() {
		return this.showName;
	}

	public int getSnatched() {
		return this.snatched;
	}

	public int getSports() {
		return this.sports;
	}

	public String getStatus() {
		return this.status;
	}

	@StringRes
	public int getStatusTranslationResource() {
		if (this.status != null) {
			String normalizedStatus = this.status.toLowerCase();

			switch (normalizedStatus) {
				case "continuing":
					return R.string.continuing;

				case "ended":
					return R.string.ended;

				case "unknown":
					return R.string.unknown;
			}
		}

		return 0;
	}

	public int getSubtitles() {
		return this.subtitles;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public int getTvRageId() {
		return this.tvRageId;
	}

	public String getTvRageName() {
		return this.tvRageName;
	}

	public void setAirByDate(int airByDate) {
		this.airByDate = airByDate;
	}

	public void setAirs(String airs) {
		this.airs = airs;
	}

	public void setAnime(int anime) {
		this.anime = anime;
	}

	public void setArchiveFirstmatch(int archiveFirstmatch) {
		this.archiveFirstmatch = archiveFirstmatch;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	public void setDvdOrder(int dvdOrder) {
		this.dvdOrder = dvdOrder;
	}

	public void setEpisodesCount(int episodesCount) {
		this.episodesCount = episodesCount;
	}

	public void setFlattenFolders(int flattenFolders) {
		this.flattenFolders = flattenFolders;
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

	public void setScene(int scene) {
		this.scene = scene;
	}

	public void setSeasonList(RealmList<RealmString> seasonList) {
		this.seasonList = seasonList;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public void setSnatched(int snatched) {
		this.snatched = snatched;
	}

	public void setSports(int sports) {
		this.sports = sports;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSubtitles(int subtitles) {
		this.subtitles = subtitles;
	}

	public void setTvDbId(int tvDbId) {
		this.tvDbId = tvDbId;
	}

	public void setTvRageId(int tvRageId) {
		this.tvRageId = tvRageId;
	}

	public void setTvRageName(String tvRageName) {
		this.tvRageName = tvRageName;
	}
}
