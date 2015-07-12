package com.mgaetan89.showsrage.model;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.mgaetan89.showsrage.R;

import java.io.Serializable;

public class History implements Serializable {
	private static final long serialVersionUID = 421953291471416097L;

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

	@StringRes
	public int getStatusTranslationResource() {
		if (this.status != null) {
			String normalizedStatus = this.status.toLowerCase();

			switch (normalizedStatus) {
				case "downloaded":
					return R.string.downloaded;

				case "snatched":
					return R.string.snatched;
			}
		}

		return 0;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public int getVersion() {
		return this.version;
	}
}
