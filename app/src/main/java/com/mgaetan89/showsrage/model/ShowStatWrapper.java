package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class ShowStatWrapper implements Serializable {
	private static final long serialVersionUID = 4826968878985690889L;

	@SerializedName("show.stats")
	private Map<Integer, ShowStats> showStats;

	public Map<Integer, ShowStats> getShowStats() {
		return this.showStats;
	}
}
