package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.ImageView;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

public class ShowPresenter {
	@Nullable
	private Context context;

	@Nullable
	private Show show;

	public ShowPresenter(@Nullable Show show, @Nullable Context context) {
		this.context = context;
		this.show = show;
	}

	public int getEpisodesCount() {
		if (this.show == null) {
			return 0;
		}

		return this.show.getEpisodesCount();
	}

	public int getEpisodesDownloaded() {
		if (this.show == null) {
			return 0;
		}

		return this.show.getDownloaded();
	}

	public int getEpisodesPending() {
		if (this.show == null) {
			return 0;
		}

		return this.show.getDownloaded() + this.show.getSnatched();
	}

	public String getName() {
		if (this.show == null) {
			return "";
		}

		return this.show.getShowName();
	}

	public String getNetworkAndQuality() {
		if (this.show == null) {
			return "";
		}

		String network = this.show.getNetwork();
		String quality = this.show.getQuality();

		if (TextUtils.isEmpty(quality)) {
			return network;
		}

		if (TextUtils.isEmpty(network)) {
			return quality;
		}

		return network + " / " + quality;
	}

	public CharSequence getNextEpisodeDateOrStatus() {
		if (this.show == null) {
			return "";
		}

		String nextEpisodeAirDate = this.show.getNextEpisodeAirDate();

		if (TextUtils.isEmpty(nextEpisodeAirDate)) {
			int status = this.show.getStatusTranslationResource();

			if (status == 0 || this.context == null) {
				return this.show.getStatus();
			}

			return this.context.getString(status);
		}

		return DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public String getPosterUrl() {
		if (this.show == null) {
			return "";
		}

		return SickRageApi.getInstance().getPosterUrl(this.show.getTvDbId(), Indexer.TVDB);
	}

	@BindingAdapter({"bind:image"})
	public static void loadImage(ImageView imageView, String url) {
		ImageLoader.load(imageView, url, true);
	}
}
