package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

public class RootDir {
	@SerializedName("default")
	private int defaultDir = 0;
	private String location = "";
	private int valid = 0;

	public int getDefaultDir() {
		return this.defaultDir;
	}

	public String getLocation() {
		return this.location;
	}

	public int getValid() {
		return this.valid;
	}
}
