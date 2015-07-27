package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class ShowStat implements Serializable {
	private static final long serialVersionUID = 7360065212498675713L;

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
		if (this.downloaded == null) {
			return this.archived;
		}

		Integer total = this.downloaded.get("total");

		if (total == null) {
			return this.archived;
		}

		return this.archived + total;
	}

	public int getTotalPending() {
		if (this.snatched == null) {
			return this.snatchedBest;
		}

		Integer total = this.snatched.get("total");

		if (total == null) {
			return this.snatchedBest;
		}

		return this.snatchedBest + total;
	}

	public int getUnaired() {
		return this.unaired;
	}

	public int getWanted() {
		return this.wanted;
	}
}
