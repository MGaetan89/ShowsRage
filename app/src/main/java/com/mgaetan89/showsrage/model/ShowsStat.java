package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

public class ShowsStat {
	@SerializedName("ep_downloaded")
	private int episodesDownloaded;
	@SerializedName("ep_snatched")
	private int episodesSnatched;
	@SerializedName("ep_total")
	private int episodesTotal;
	@SerializedName("shows_active")
	private int showsActive;
	@SerializedName("shows_total")
	private int showsTotal;

	public int getEpisodesDownloaded() {
		return this.episodesDownloaded;
	}

	public int getEpisodesMissing() {
		return this.episodesTotal - (this.episodesDownloaded + this.episodesSnatched);
	}

	public int getEpisodesSnatched() {
		return this.episodesSnatched;
	}

	public int getEpisodesTotal() {
		return this.episodesTotal;
	}

	public int getShowsActive() {
		return this.showsActive;
	}

	public int getShowsTotal() {
		return this.showsTotal;
	}
}
