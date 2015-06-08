package com.mgaetan89.showsrage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
	private static final long serialVersionUID = 7212348168360976628L;

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
