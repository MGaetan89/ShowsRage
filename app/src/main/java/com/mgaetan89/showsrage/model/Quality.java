package com.mgaetan89.showsrage.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Quality implements Parcelable {
	private List<String> archive = null;
	private List<String> initial = null;

	public Quality() {
	}

	protected Quality(Parcel in) {
		this.archive = in.createStringArrayList();
		this.initial = in.createStringArrayList();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public List<String> getArchive() {
		return this.archive;
	}

	public List<String> getInitial() {
		return this.initial;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringList(this.archive);
		dest.writeStringList(this.initial);
	}

	public static final Parcelable.Creator<Quality> CREATOR = new Parcelable.Creator<Quality>() {
		@Override
		public Quality createFromParcel(Parcel in) {
			return new Quality(in);
		}

		@Override
		public Quality[] newArray(int size) {
			return new Quality[size];
		}
	};
}
