package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Schedule;
import com.mgaetan89.showsrage.network.SickRageApi;

public class SchedulePresenter {
	@Nullable
	private Context context;

	@Nullable
	private Schedule schedule;

	public SchedulePresenter(@Nullable Schedule schedule, @Nullable Context context) {
		this.context = context;
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

	@Nullable
	public CharSequence getAirDateTime() {
		CharSequence airDate = this.getAirDate();
		CharSequence airTime = this.getAirTime();

		if (airDate == null && airTime == null) {
			return null;
		}

		if (airDate != null) {
			if (airTime != null) {
				return airDate + ", " + airTime;
			}

			return airDate;
		}

		return airTime;
	}

	@Nullable
	public CharSequence getAirTime() {
		if (this.context == null || this.schedule == null) {
			return null;
		}

		String airDate = this.schedule.getAirDate();
		String airTime = this.getAirTimeOnly();

		if (airDate == null || airDate.isEmpty() || airTime == null || airTime.isEmpty()) {
			return null;
		}

		return DateTimeHelper.getLocalizedTime(this.context, airDate + " " + airTime, "yyyy-MM-dd K:mm a");
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

	@Nullable
	/* package */ String getAirTimeOnly() {
		if (this.schedule == null) {
			return null;
		}

		String airTime = this.schedule.getAirs();

		if (airTime == null || airTime.isEmpty()) {
			return null;
		}

		return airTime.replaceFirst("(?i)^(monday|tuesday|wednesday|thursday|friday|saturday|sunday) ", "");
	}
}
