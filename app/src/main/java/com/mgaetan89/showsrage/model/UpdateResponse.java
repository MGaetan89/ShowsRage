package com.mgaetan89.showsrage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class UpdateResponse implements Parcelable {
	@SerializedName("commits_offset")
	private int commitsOffset = 0;

	@SerializedName("current_version")
	private Version currentVersion = null;

	@SerializedName("latest_version")
	private Version latestVersion = null;

	@SerializedName("needs_update")
	private boolean needsUpdate = false;

	public UpdateResponse() {
	}

	protected UpdateResponse(Parcel in) {
		this.commitsOffset = in.readInt();
		this.currentVersion = (Version) in.readValue(Version.class.getClassLoader());
		this.latestVersion = (Version) in.readValue(Version.class.getClassLoader());
		this.needsUpdate = in.readByte() != 0;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public int getCommitsOffset() {
		return this.commitsOffset;
	}

	public Version getCurrentVersion() {
		return this.currentVersion;
	}

	public Version getLatestVersion() {
		return this.latestVersion;
	}

	public boolean needsUpdate() {
		return this.needsUpdate;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.commitsOffset);
		dest.writeValue(this.currentVersion);
		dest.writeValue(this.latestVersion);
		dest.writeByte(this.needsUpdate ? (byte) 1 : (byte) 0);
	}

	public static final Creator<UpdateResponse> CREATOR = new Creator<UpdateResponse>() {
		@Override
		public UpdateResponse createFromParcel(Parcel in) {
			return new UpdateResponse(in);
		}

		@Override
		public UpdateResponse[] newArray(int size) {
			return new UpdateResponse[size];
		}
	};
}
