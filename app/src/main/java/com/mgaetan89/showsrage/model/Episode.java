package com.mgaetan89.showsrage.model;

import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.mgaetan89.showsrage.R;

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

	private int number;

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

	public int getNumber() {
		return this.number;
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
	public static String getStatusForMenuId(@IdRes int menuId) {
		switch (menuId) {
			case R.id.menu_episode_set_status_archived:
				return "archived";

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

	public void setNumber(int number) {
		this.number = number;
	}
}
