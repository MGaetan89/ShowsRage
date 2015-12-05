package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {
	@SerializedName("langid")
	private int langId;
	private List<SearchResultItem> results;

	public int getLangId() {
		return this.langId;
	}

	public List<SearchResultItem> getResults() {
		return this.results;
	}
}
