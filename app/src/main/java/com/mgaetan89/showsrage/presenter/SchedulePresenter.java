package com.mgaetan89.showsrage.presenter;

import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Schedule;
import com.mgaetan89.showsrage.network.SickRageApi;

public class SchedulePresenter {
	@Nullable
	private Schedule schedule;

	public SchedulePresenter(@Nullable Schedule schedule) {
		this.schedule = schedule;
	}

	@Nullable
	public CharSequence getAirDate() {
		if (this.schedule == null) {
			return null;
		}

		String airDate = this.schedule.getAirDate();

		if (airDate == null || airDate.isEmpty()) {
			return null;
		}

		return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public int getEpisode() {
		if (this.schedule == null) {
			return 0;
		}

		return this.schedule.getEpisode();
	}

	public String getNetwork() {
		if (this.schedule == null) {
			return "";
		}

		return this.schedule.getNetwork();
	}

	public String getPosterUrl() {
		if (this.schedule == null) {
			return "";
		}

		return SickRageApi.getInstance().getPosterUrl(this.schedule.getTvDbId(), Indexer.TVDB);
	}

	public String getQuality() {
		if (this.schedule == null) {
			return "";
		}

		return this.schedule.getQuality();
	}

	public int getSeason() {
		if (this.schedule == null) {
			return 0;
		}

		return this.schedule.getSeason();
	}

	public String getShowName() {
		if (this.schedule == null) {
			return "";
		}

		return this.schedule.getShowName();
	}
}
