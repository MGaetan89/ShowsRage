package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ShowStatWrapper {
	@SerializedName("show.stats")
	private Map<Integer, ShowStats> showStats;

	public Map<Integer, ShowStats> getShowStats() {
		return this.showStats;
	}
}
