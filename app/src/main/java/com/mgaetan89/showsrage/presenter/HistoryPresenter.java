package com.mgaetan89.showsrage.presenter;

import android.support.annotation.Nullable;

import com.mgaetan89.showsrage.model.History;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.network.SickRageApi;

public class HistoryPresenter {
	@Nullable
	private History history;

	public HistoryPresenter(@Nullable History history) {
		this.history = history;
	}

	public int getEpisode() {
		if (this.history == null) {
			return 0;
		}

		return this.history.getEpisode();
	}

	public String getPosterUrl() {
		if (this.history == null) {
			return "";
		}

		return SickRageApi.getInstance().getPosterUrl(this.history.getTvDbId(), Indexer.TVDB);
	}

	public String getProvider() {
		if (this.history == null) {
			return "";
		}

		return this.history.getProvider();
	}

	@Nullable
	public String getProviderQuality() {
		if (this.history == null) {
			return null;
		}

		String provider = this.history.getProvider();

		if ("-1".equals(provider)) {
			return this.history.getQuality();
		}

		return null;
	}

	public String getQuality() {
		if (this.history == null) {
			return "";
		}

		return this.history.getQuality();
	}

	public int getSeason() {
		if (this.history == null) {
			return 0;
		}

		return this.history.getSeason();
	}

	public String getShowName() {
		if (this.history == null) {
			return "";
		}

		return this.history.getShowName();
	}
}
