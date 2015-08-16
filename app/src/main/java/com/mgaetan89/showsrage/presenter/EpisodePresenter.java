package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.fragment.EpisodeDetailFragment;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.Episode;

public class EpisodePresenter {
	@Nullable
	private Context context = null;

	@Nullable
	private Episode episode = null;

	@Nullable
	private EpisodeDetailFragment searchListener = null;

	public EpisodePresenter(@Nullable Episode episode, @Nullable Context context) {
		this.context = context;
		this.episode = episode;
	}

	public CharSequence getAirDate() {
		if (this.episode == null) {
			return "";
		}

		String airDate = this.episode.getAirDate();

		if (TextUtils.isEmpty(airDate)) {
			if (this.context != null) {
				return this.context.getString(R.string.never);
			}

			return "";
		}

		return DateTimeHelper.getRelativeDate(airDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
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

	public int getNumber() {
		if (this.episode == null) {
			return 0;
		}

		return this.episode.getNumber();
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

		String quality = this.episode.getQuality();

		if ("N/A".equalsIgnoreCase(quality)) {
			return "";
		}

		return quality;
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

		if (status != 0 && this.context != null) {
			return this.context.getString(status);
		}

		return this.episode.getStatus();
	}

	@ColorInt
	public int getStatusColor() {
		if (this.episode == null) {
			return Color.TRANSPARENT;
		}

		if (this.context != null) {
			return this.context.getResources().getColor(this.episode.getStatusBackgroundColor());
		}

		return Color.TRANSPARENT;
	}

	public int getStatusVisibility() {
		if (this.episode == null || TextUtils.isEmpty(this.episode.getStatus())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public void searchEpisode(View view) {
		if (this.searchListener != null) {
			this.searchListener.searchEpisode();
		}
	}

	@BindingAdapter({"bind:backgroundTint"})
	public static void setBackgroundTint(TextView textView, @ColorInt int backgroundTint) {
		Drawable background = DrawableCompat.wrap(textView.getBackground());
		DrawableCompat.setTint(background, backgroundTint);
	}

	public void setEpisode(@Nullable Episode episode) {
		this.episode = episode;
	}

	public void setSearchListener(@Nullable EpisodeDetailFragment searchListener) {
		this.searchListener = searchListener;
	}
}
