package com.mgaetan89.showsrage.presenter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;

import com.mgaetan89.showsrage.fragment.EpisodeDetailFragment;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Episode;

public class EpisodePresenter {
	@Nullable
	private Episode episode = null;

	@Nullable
	private EpisodeDetailFragment fragment = null;

	public EpisodePresenter(@Nullable Episode episode, @Nullable EpisodeDetailFragment fragment) {
		this.episode = episode;
		this.fragment = fragment;
	}

	public CharSequence getAirDate() {
		if (this.episode == null) {
			return "";
		}

		return DateTimeHelper.getRelativeDate(this.episode.getAirDate(), "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public int getAirDateVisibility() {
		if (this.episode == null || TextUtils.isEmpty(this.episode.getAirDate())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getFileSize() {
		if (this.episode == null) {
			return "";
		}

		return this.episode.getFileSizeHuman();
	}

	public int getFileSizeLocationVisibility() {
		if (this.episode == null || this.episode.getFileSize() <= 0L) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getLocation() {
		if (this.episode == null) {
			return "";
		}

		return this.episode.getLocation();
	}

	public String getName() {
		if (this.episode == null) {
			return "";
		}

		return this.episode.getName();
	}

	public int getNameVisibility() {
		if (this.episode == null || TextUtils.isEmpty(this.episode.getName())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getPlot() {
		if (this.episode == null) {
			return "";
		}

		return this.episode.getDescription();
	}

	public int getPlotVisibility() {
		if (this.episode == null || TextUtils.isEmpty(this.episode.getDescription())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getQuality() {
		if (this.episode == null) {
			return "";
		}

		return this.episode.getQuality();
	}

	public int getQualityVisibility() {
		if (this.episode == null || "N/A".equalsIgnoreCase(this.episode.getQuality())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getStatus() {
		if (this.episode == null) {
			return "";
		}

		int status = this.episode.getStatusTranslationResource();

		if (status != 0 && this.fragment != null) {
			return this.fragment.getString(status);
		}

		return this.episode.getStatus();
	}

	public int getStatusVisibility() {
		if (this.episode == null || TextUtils.isEmpty(this.episode.getStatus())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public void searchEpisode(View view) {
		if (this.fragment != null) {
			this.fragment.searchEpisode();
		}
	}

	public void setEpisode(@Nullable Episode episode) {
		this.episode = episode;
	}
}
