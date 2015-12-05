package com.mgaetan89.showsrage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Version implements Parcelable {
	private String branch = "";
	private String commit = "";
	private String version = "";

	public Version() {
	}

	protected Version(Parcel in) {
		this.branch = in.readString();
		this.commit = in.readString();
		this.version = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getBranch() {
		return this.branch;
	}

	public String getCommit() {
		return this.commit;
	}

	public String getVersion() {
		return this.version;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.branch);
		dest.writeString(this.commit);
		dest.writeString(this.version);
	}

	public static final Creator<Version> CREATOR = new Creator<Version>() {
		@Override
		public Version createFromParcel(Parcel in) {
			return new Version(in);
		}

		@Override
		public Version[] newArray(int size) {
			return new Version[size];
		}
	};
}
