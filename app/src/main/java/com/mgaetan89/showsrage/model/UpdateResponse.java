package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateResponse implements Serializable {
	private static final long serialVersionUID = -2029310491059530061L;

	@SerializedName("commits_offset")
	private int commitsOffset;

	@SerializedName("current_version")
	private Version currentVersion;

	@SerializedName("latest_version")
	private Version latestVersion;

	@SerializedName("needs_update")
	private boolean needsUpdate;

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
}
