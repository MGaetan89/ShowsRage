package com.mgaetan89.showsrage.presenter;

import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.ImageView;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.ComingEpisode;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.network.SickRageApi;

public class ComingEpisodePresenter {
	@Nullable
	private ComingEpisode comingEpisode;

	public ComingEpisodePresenter(@Nullable ComingEpisode comingEpisode) {
		this.comingEpisode = comingEpisode;
	}

	public CharSequence getAirDate() {
		if (this.comingEpisode == null) {
			return "";
		}

		String airDate = this.comingEpisode.getAirDate();

		if (TextUtils.isEmpty(airDate)) {
			return "Never"; // TODO R.string.never;
		}

		return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public int getEpisode() {
		if (this.comingEpisode == null) {
			return 0;
		}

		return this.comingEpisode.getEpisode();
	}

	public String getEpisodeName() {
		if (this.comingEpisode == null) {
			return "";
		}

		return this.comingEpisode.getEpisodeName();
	}

	public String getNetworkAndQuality() {
		if (this.comingEpisode == null) {
			return "";
		}

		return this.comingEpisode.getNetwork() + " / " + this.comingEpisode.getQuality();
	}

	public String getPosterUrl() {
		if (this.comingEpisode == null) {
			return "";
		}

		return SickRageApi.getInstance().getPosterUrl(this.comingEpisode.getTvDbId(), Indexer.TVDB);
	}

	public int getSeason() {
		if (this.comingEpisode == null) {
			return 0;
		}

		return this.comingEpisode.getSeason();
	}

	public String getShowName() {
		if (this.comingEpisode == null) {
			return "";
		}

		return this.comingEpisode.getShowName();
	}

	@BindingAdapter({"bind:image"})
	public static void loadImage(ImageView imageView, String url) {
		ImageLoader.load(imageView, url, true);
	}
}
