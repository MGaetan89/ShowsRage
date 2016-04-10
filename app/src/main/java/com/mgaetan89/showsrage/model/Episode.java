package com.mgaetan89.showsrage.model;

import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.mgaetan89.showsrage.R;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Episode extends RealmObject {
	@SerializedName("airdate")
	private String airDate = "";
	private String description = "";
	@SerializedName("file_size")
	private long fileSize = 0L;
	@SerializedName("file_size_human")
	private String fileSizeHuman = "";
	@PrimaryKey
	private String id = "";
	private int indexerId = 0;
	private String location = "";
	private String name = "";
	private int number = 0;
	private String quality = "";
	@SerializedName("release_name")
	private String releaseName = "";
	private int season = 0;
	private String status = "";
	private String subtitles = "";

	public static String buildId(int indexer, int season, int episode) {
		return indexer + "_" + season + "_" + episode;
	}

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

	public String getId() {
		return this.id;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public String getLocation() {
		return this.location;
	}

	public String getName() {
		return this.name;
	}

	public int getNumber() {
		return this.number;
	}

	public String getQuality() {
		return this.quality;
	}

	public String getReleaseName() {
		return this.releaseName;
	}

	public int getSeason() {
		return this.season;
	}

	public String getStatus() {
		return this.status;
	}

	@ColorRes
	public int getStatusBackgroundColor() {
		if (this.status != null) {
			String normalizedStatus = this.status.toLowerCase();

			switch (normalizedStatus) {
				case "archived":
				case "downloaded":
					return R.color.green;

				case "ignored":
				case "skipped":
					return R.color.blue;

				case "snatched":
				case "snatched (proper)":
					return R.color.purple;

				case "unaired":
					return R.color.yellow;

				case "wanted":
					return R.color.red;
			}
		}

		return android.R.color.transparent;
	}

	@StringRes
	public int getStatusTranslationResource() {
		if (this.status != null) {
			String normalizedStatus = this.status.toLowerCase();

			switch (normalizedStatus) {
				case "archived":
					return R.string.archived;

				case "downloaded":
					return R.string.downloaded;

				case "ignored":
					return R.string.ignored;

				case "skipped":
					return R.string.skipped;

				case "snatched":
					return R.string.snatched;

				case "snatched (proper)":
					return R.string.snatched_proper;

				case "unaired":
					return R.string.unaired;

				case "wanted":
					return R.string.wanted;
			}
		}

		return 0;
	}

	@Nullable
	public static String getStatusForMenuId(@IdRes Integer menuId) {
		switch (menuId) {
			case R.id.menu_episode_set_status_failed:
				return "failed";

			case R.id.menu_episode_set_status_ignored:
				return "ignored";

			case R.id.menu_episode_set_status_skipped:
				return "skipped";

			case R.id.menu_episode_set_status_wanted:
				return "wanted";
		}

		return null;
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

	public void setId(String id) {
		this.id = id;
	}

	public void setIndexerId(int indexerId) {
		this.indexerId = indexerId;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}
}
