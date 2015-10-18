package com.mgaetan89.showsrage.presenter;

import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Episode;

public class EpisodePresenter {
	@Nullable
	private Episode episode;

	public EpisodePresenter(@Nullable Episode episode) {
		this.episode = episode;
	}

	@Nullable
	public CharSequence getAirDate() {
		if (this.episode == null) {
			return null;
		}

		String airDate = this.episode.getAirDate();

		if (airDate == null || airDate.isEmpty()) {
			return null;
		}

		return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public String getQuality() {
		if (this.episode == null) {
			return "";
		}

		String quality = this.episode.getQuality();

		if ("N/A".equalsIgnoreCase(quality)) {
			return "";
		}

		return quality;
	}

	@ColorRes
	public int getStatusColor() {
		if (this.episode == null) {
			return android.R.color.transparent;
		}

		return this.episode.getStatusBackgroundColor();
	}
}
