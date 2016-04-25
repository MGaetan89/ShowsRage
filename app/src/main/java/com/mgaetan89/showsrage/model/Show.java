package com.mgaetan89.showsrage.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.mgaetan89.showsrage.R;

import java.util.ArrayList;
import java.util.List;

public class Show implements Parcelable {
	@SerializedName("air_by_date")
	private int airByDate = 0;
	private String airs = "";
	private int anime = 0;
	@SerializedName("archive_firstmatch")
	private int archiveFirstmatch = 0;
	private int downloaded = 0;
	@SerializedName("dvdorder")
	private int dvdOrder = 0;
	private int episodesCount = 0;
	@SerializedName("flatten_folders")
	private int flattenFolders = 0;
	private List<String> genre = null;
	@SerializedName("imdbid")
	private String imdbId = "";
	@SerializedName("indexerid")
	private int indexerId = 0;
	private String language = "";
	private String location = "";
	private String network = "";
	@SerializedName("next_ep_airdate")
	private String nextEpisodeAirDate = "";
	private int paused = 0;
	private String quality = "";
	@SerializedName("quality_details")
	private Quality qualityDetails = null;
	private int scene = 0;
	@SerializedName("season_list")
	private List<Integer> seasonList = new ArrayList<>();
	@SerializedName("show_name")
	private String showName = "";
	private int snatched = 0;
	private int sports = 0;
	private String status = "";
	private int subtitles = 0;
	@SerializedName("tvdbid")
	private int tvDbId = 0;
	@SerializedName("tvrage_id")
	private int tvRageId = 0;
	@SerializedName("tvrage_name")
	private String tvRageName = "";

	public Show() {
	}

	protected Show(Parcel in) {
		this.airByDate = in.readInt();
		this.airs = in.readString();
		this.anime = in.readInt();
		this.archiveFirstmatch = in.readInt();
		this.downloaded = in.readInt();
		this.dvdOrder = in.readInt();
		this.episodesCount = in.readInt();
		this.flattenFolders = in.readInt();
		this.genre = in.createStringArrayList();
		this.imdbId = in.readString();
		this.indexerId = in.readInt();
		this.language = in.readString();
		this.location = in.readString();
		this.network = in.readString();
		this.nextEpisodeAirDate = in.readString();
		this.paused = in.readInt();
		this.quality = in.readString();
		this.qualityDetails = (Quality) in.readValue(Quality.class.getClassLoader());
		this.scene = in.readInt();
		in.readList(this.seasonList, null);
		this.showName = in.readString();
		this.snatched = in.readInt();
		this.sports = in.readInt();
		this.status = in.readString();
		this.subtitles = in.readInt();
		this.tvDbId = in.readInt();
		this.tvRageId = in.readInt();
		this.tvRageName = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public int getAirByDate() {
		return this.airByDate;
	}

	public String getAirs() {
		return this.airs;
	}

	public int getAnime() {
		return this.anime;
	}

	public int getArchiveFirstmatch() {
		return this.archiveFirstmatch;
	}

	public int getDownloaded() {
		return this.downloaded;
	}

	public int getDvdOrder() {
		return this.dvdOrder;
	}

	public int getEpisodesCount() {
		return this.episodesCount;
	}

	public int getFlattenFolders() {
		return this.flattenFolders;
	}

	public List<String> getGenre() {
		return this.genre;
	}

	public String getImdbId() {
		return this.imdbId;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public String getLanguage() {
		return this.language;
	}

	public String getLocation() {
		return this.location;
	}

	public String getNetwork() {
		return this.network;
	}

	public String getNextEpisodeAirDate() {
		return this.nextEpisodeAirDate;
	}

	public int getPaused() {
		return this.paused;
	}

	public String getQuality() {
		return this.quality;
	}

	public Quality getQualityDetails() {
		return this.qualityDetails;
	}

	public int getScene() {
		return this.scene;
	}

	public List<Integer> getSeasonList() {
		return this.seasonList;
	}

	public String getShowName() {
		return this.showName;
	}

	public int getSnatched() {
		return this.snatched;
	}

	public int getSports() {
		return this.sports;
	}

	public String getStatus() {
		return this.status;
	}

	@StringRes
	public int getStatusTranslationResource() {
		if (this.status != null) {
			String normalizedStatus = this.status.toLowerCase();

			switch (normalizedStatus) {
				case "continuing":
					return R.string.continuing;

				case "ended":
					return R.string.ended;

				case "unknown":
					return R.string.unknown;
			}
		}

		return 0;
	}

	public int getSubtitles() {
		return this.subtitles;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public int getTvRageId() {
		return this.tvRageId;
	}

	public String getTvRageName() {
		return this.tvRageName;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	public void setEpisodesCount(int episodesCount) {
		this.episodesCount = episodesCount;
	}

	public void setSnatched(int snatched) {
		this.snatched = snatched;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.airByDate);
		dest.writeString(this.airs);
		dest.writeInt(this.anime);
		dest.writeInt(this.archiveFirstmatch);
		dest.writeInt(this.downloaded);
		dest.writeInt(this.dvdOrder);
		dest.writeInt(this.episodesCount);
		dest.writeInt(this.flattenFolders);
		dest.writeStringList(this.genre);
		dest.writeString(this.imdbId);
		dest.writeInt(this.indexerId);
		dest.writeString(this.language);
		dest.writeString(this.location);
		dest.writeString(this.network);
		dest.writeString(this.nextEpisodeAirDate);
		dest.writeInt(this.paused);
		dest.writeString(this.quality);
		dest.writeValue(this.qualityDetails);
		dest.writeInt(this.scene);
		dest.writeList(this.seasonList);
		dest.writeString(this.showName);
		dest.writeInt(this.snatched);
		dest.writeInt(this.sports);
		dest.writeString(this.status);
		dest.writeInt(this.subtitles);
		dest.writeInt(this.tvDbId);
		dest.writeInt(this.tvRageId);
		dest.writeString(this.tvRageName);
	}

	public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>() {
		@Override
		public Show createFromParcel(Parcel in) {
			return new Show(in);
		}

		@Override
		public Show[] newArray(int size) {
			return new Show[size];
		}
	};
}
