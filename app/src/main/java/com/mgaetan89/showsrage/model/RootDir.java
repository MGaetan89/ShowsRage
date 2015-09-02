package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RootDir implements Serializable {
	private static final long serialVersionUID = 4775229023725060662L;

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
