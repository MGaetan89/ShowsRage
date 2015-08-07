package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.History;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.network.SickRageApi;

public class HistoryPresenter {
	@Nullable
	private Context context = null;

	@Nullable
	private History history = null;

	public HistoryPresenter(@Nullable History history, @Nullable Context context) {
		this.context = context;
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

	public String getProviderAndQuality() {
		if (this.history == null) {
			return "";
		}

		String provider = this.history.getProvider();

		if ("-1".equals(provider)) {
			return this.history.getQuality();
		}

		return this.context.getResources().getString(R.string.provider_quality, provider, this.history.getQuality());
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

	public CharSequence getStatusAndAirDate() {
		if (this.history == null) {
			return "";
		}

		int status = this.history.getStatusTranslationResource();
		CharSequence airDate = DateTimeHelper.getRelativeDate(this.history.getDate(), "yyyy-MM-dd hh:mm", 0);
		String statusString = this.history.getStatus();

		if (status != 0 && this.context != null) {
			statusString = this.context.getResources().getString(status);
		}

		return statusString + " " + airDate.toString().toLowerCase();
	}

	@BindingAdapter({"bind:image"})
	public static void loadImage(ImageView imageView, String url) {
		ImageLoader.load(imageView, url, true);
	}
}
