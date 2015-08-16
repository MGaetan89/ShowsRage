package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.helper.ImageLoader;
import com.mgaetan89.showsrage.model.Indexer;
import com.mgaetan89.showsrage.model.Quality;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShowPresenter {
	@Nullable
	private Context context = null;

	@Nullable
	private Palette.PaletteAsyncListener paletteListener = null;

	@Nullable
	private Show show = null;

	public ShowPresenter(@Nullable Show show, @Nullable Context context) {
		this.context = context;
		this.show = show;
	}

	public String getAirDate() {
		if (this.show == null) {
			return "N/A";
		}

		String airDate = this.show.getAirs();

		if (TextUtils.isEmpty(airDate)) {
			return "N/A";
		}

		return airDate;
	}

	public int getAirDateVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getAirs())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getBannerUrl() {
		if (this.show == null) {
			return "";
		}

		return SickRageApi.getInstance().getBannerUrl(this.show.getTvDbId(), Indexer.TVDB);
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

	public String getFanArtUrl() {
		if (this.show == null) {
			return "";
		}

		return SickRageApi.getInstance().getFanArtUrl(this.show.getTvDbId(), Indexer.TVDB);
	}

	public String getGenre() {
		if (this.show == null) {
			return "";
		}

		return listToString(this.show.getGenre());
	}

	public int getGenreVisibility() {
		if (this.show == null || this.show.getGenre() == null || this.show.getGenre().isEmpty()) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public int getImdbVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getImdbId())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getLocation() {
		if (this.show == null) {
			return "N/A";
		}

		String location = this.show.getLocation();

		if (TextUtils.isEmpty(location)) {
			return "N/A";
		}

		return location;
	}

	public int getLocationVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getLocation())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getName() {
		if (this.show == null) {
			return "";
		}

		return this.show.getShowName();
	}

	public int getNameVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getShowName())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getNetwork() {
		if (this.show == null) {
			return "";
		}

		return this.show.getNetwork();
	}

	public int getNetworkVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getNetwork())) {
			return View.GONE;
		}

		return View.VISIBLE;
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

	public CharSequence getNextEpisodeDate() {
		if (this.show == null) {
			return "";
		}

		return DateTimeHelper.getRelativeDate(this.show.getNextEpisodeAirDate(), "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public int getNextEpisodeDateVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getNextEpisodeAirDate())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public CharSequence getNextEpisodeDateOrStatus() {
		if (this.show == null) {
			return "";
		}

		if (TextUtils.isEmpty(this.show.getNextEpisodeAirDate())) {
			return this.getStatus();
		}

		return this.getNextEpisodeDate();
	}

	@Nullable
	public Palette.PaletteAsyncListener getPaletteListener() {
		return this.paletteListener;
	}

	public String getPosterUrl() {
		if (this.show == null) {
			return "";
		}

		return SickRageApi.getInstance().getPosterUrl(this.show.getTvDbId(), Indexer.TVDB);
	}

	public String getQuality() {
		if (this.context == null || this.show == null) {
			return "";
		}

		String quality = this.show.getQuality();

		if ("custom".equalsIgnoreCase(quality)) {
			Quality qualityDetails = this.show.getQualityDetails();
			String allowed = listToString(this.getTranslatedQualities(qualityDetails.getInitial(), true));
			String preferred = listToString(this.getTranslatedQualities(qualityDetails.getArchive(), false));

			return this.context.getString(R.string.quality_custom, allowed, preferred);
		}

		return this.context.getString(R.string.quality, quality);
	}

	public int getQualityVisibility() {
		if (this.show == null || TextUtils.isEmpty(this.show.getQuality())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public String getStatus() {
		if (this.show == null) {
			return "";
		}

		int status = this.show.getStatusTranslationResource();

		if (status == 0 || this.context == null) {
			return this.show.getStatus();
		}

		return this.context.getString(status);
	}

	public int getStatusVisibility() {
		if (this.show == null || !TextUtils.isEmpty(this.show.getNextEpisodeAirDate())) {
			return View.GONE;
		}

		return View.VISIBLE;
	}

	public boolean isCircleBanner() {
		return false;
	}

	public boolean isCircleFanArt() {
		return false;
	}

	public boolean isCircleLogo() {
		return true;
	}

	public boolean isCirclePoster() {
		return false;
	}

	@BindingAdapter({"bind:circle", "bind:image"})
	public static void loadImage(ImageView imageView, String url, boolean circle) {
		ImageLoader.load(imageView, url, circle, null);
	}

	@BindingAdapter({"bind:circle", "bind:image", "bind:paletteListener"})
	public static void loadImage(ImageView imageView, String url, boolean circle, Palette.PaletteAsyncListener paletteListener) {
		ImageLoader.load(imageView, url, circle, paletteListener);
	}

	public void openImdb(View view) {
		if (this.context == null || this.show == null) {
			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.imdb.com/title/" + this.show.getImdbId()));

		this.context.startActivity(intent);
	}

	public void openTheTvdb(View view) {
		if (this.context == null || this.show == null) {
			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://thetvdb.com/?tab=series&id=" + this.show.getTvDbId()));

		this.context.startActivity(intent);
	}

	public void openTvrage(View view) {
		if (this.context == null || this.show == null) {
			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("http://www.tvrage.com/show/id-" + this.show.getTvRageId()));

		this.context.startActivity(intent);
	}

	public void setPaletteListener(@Nullable Palette.PaletteAsyncListener paletteListener) {
		this.paletteListener = paletteListener;
	}

	@NonNull
	private List<String> getTranslatedQualities(@Nullable Collection<String> qualities, boolean allowed) {
		List<String> translatedQualities = new ArrayList<>();

		if (this.context == null || qualities == null || qualities.isEmpty()) {
			return translatedQualities;
		}

		List<String> keys;
		List<String> values;
		Resources resources = this.context.getResources();

		if (allowed) {
			keys = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_keys));
			values = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_values));
		} else {
			keys = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_keys));
			values = Arrays.asList(resources.getStringArray(R.array.allowed_qualities_values));
		}

		for (String quality : qualities) {
			int position = keys.indexOf(quality);

			if (position != -1) {
				// Skip the "Ignore" first item
				translatedQualities.add(values.get(position + 1));
			}
		}

		return translatedQualities;
	}

	@NonNull
	private static String listToString(@Nullable List<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		StringBuilder builder = new StringBuilder();

		for (int i = 0, n = list.size(); i < n; i++) {
			builder.append(list.get(i));

			if (i < n - 1) {
				builder.append(", ");
			}
		}

		return builder.toString();
	}
}
