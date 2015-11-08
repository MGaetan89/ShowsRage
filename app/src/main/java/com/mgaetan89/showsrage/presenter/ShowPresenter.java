package com.mgaetan89.showsrage.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

public class ShowPresenter {
	@Nullable
	private Show show;

	public ShowPresenter(@Nullable Show show) {
		this.show = show;
	}

	public int getDownloaded() {
		if (this.show == null) {
			return 0;
		}

		return this.show.getDownloaded();
	}

	public int getEpisodesCount() {
		if (this.show == null) {
			return 0;
		}

		return this.show.getEpisodesCount();
	}

	public String getNetwork() {
		if (this.show == null) {
			return "";
		}

		return this.show.getNetwork();
	}

	@StringRes
	public int getPauseActiveStatusRes() {
		if (this.show == null) {
			return 0;
		}

		if (this.show.getPaused() == 0) {
			return R.string.active;
		}

		return R.string.paused;
	}

	public String getPosterUrl() {
		if (this.show == null) {
			return "";
		}

		return SickRageApi.getInstance().getPosterUrl(this.show.getTvDbId(), Indexer.TVDB);
	}

	public String getQuality() {
		if (this.show == null) {
			return "";
		}

		return this.show.getQuality();
	}

	public String getShowName() {
		if (this.show == null) {
			return "";
		}

		return this.show.getShowName();
	}

	public int getSnatched() {
		if (this.show == null) {
			return 0;
		}

		return this.show.getSnatched();
	}
}
