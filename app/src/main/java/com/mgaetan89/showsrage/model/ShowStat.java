package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ShowStat {
	private int archived;
	private Map<String, Integer> downloaded;
	private int failed;
	private int ignored;
	private int skipped;
	private Map<String, Integer> snatched;
	@SerializedName("snatched_best")
	private int snatchedBest;
	private int subtitled;
	private int total;
	private int unaired;
	private int wanted;

	public int getArchived() {
		return this.archived;
	}

	public Map<String, Integer> getDownloaded() {
		return this.downloaded;
	}

	public int getFailed() {
		return this.failed;
	}

	public int getIgnored() {
		return this.ignored;
	}

	public int getSkipped() {
		return this.skipped;
	}

	public Map<String, Integer> getSnatched() {
		return this.snatched;
	}

	public int getSnatchedBest() {
		return this.snatchedBest;
	}

	public int getSubtitled() {
		return this.subtitled;
	}

	public int getTotal() {
		return this.total;
	}

	public int getTotalDone() {
		return this.archived + this.downloaded.get("total");
	}

	public int getTotalPending() {
		return this.snatchedBest + this.snatched.get("total");
	}

	public int getUnaired() {
		return this.unaired;
	}

	public int getWanted() {
		return this.wanted;
	}
}
