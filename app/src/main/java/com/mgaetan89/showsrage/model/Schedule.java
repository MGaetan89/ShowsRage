package com.mgaetan89.showsrage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Schedule implements Parcelable {
	@SerializedName("airdate")
	private String airDate;
	private String airs;
	private int episode;
	@SerializedName("ep_name")
	private String episodeName;
	@SerializedName("ep_plot")
	private String episodePlot;
	@SerializedName("indexerid")
	private int indexerId;
	private String network;
	private int paused;
	private String quality;
	private int season;
	@SerializedName("show_name")
	private String showName;
	@SerializedName("show_status")
	private String showStatus;
	@SerializedName("tvdbid")
	private int tvDbId;
	private int weekday;

	public Schedule() {
	}

	protected Schedule(Parcel in) {
		this.airDate = in.readString();
		this.airs = in.readString();
		this.episode = in.readInt();
		this.episodeName = in.readString();
		this.episodePlot = in.readString();
		this.indexerId = in.readInt();
		this.network = in.readString();
		this.paused = in.readInt();
		this.quality = in.readString();
		this.season = in.readInt();
		this.showName = in.readString();
		this.showStatus = in.readString();
		this.tvDbId = in.readInt();
		this.weekday = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getAirDate() {
		return this.airDate;
	}

	public String getAirs() {
		return this.airs;
	}

	public int getEpisode() {
		return this.episode;
	}

	public String getEpisodeName() {
		return this.episodeName;
	}

	public String getEpisodePlot() {
		return this.episodePlot;
	}

	public int getIndexerId() {
		return this.indexerId;
	}

	public String getNetwork() {
		return this.network;
	}

	public int getPaused() {
		return this.paused;
	}

	public String getQuality() {
		return this.quality;
	}

	public int getSeason() {
		return this.season;
	}

	public String getShowName() {
		return this.showName;
	}

	public String getShowStatus() {
		return this.showStatus;
	}

	public int getTvDbId() {
		return this.tvDbId;
	}

	public int getWeekday() {
		return this.weekday;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.airDate);
		dest.writeString(this.airs);
		dest.writeInt(this.episode);
		dest.writeString(this.episodeName);
		dest.writeString(this.episodePlot);
		dest.writeInt(this.indexerId);
		dest.writeString(this.network);
		dest.writeInt(this.paused);
		dest.writeString(this.quality);
		dest.writeInt(this.season);
		dest.writeString(this.showName);
		dest.writeString(this.showStatus);
		dest.writeInt(this.tvDbId);
		dest.writeInt(this.weekday);
	}

	public static final Creator<Schedule> CREATOR = new Creator<Schedule>() {
		@Override
		public Schedule createFromParcel(Parcel in) {
			return new Schedule(in);
		}

		@Override
		public Schedule[] newArray(int size) {
			return new Schedule[size];
		}
	};
}
