package com.mgaetan89.showsrage.presenter;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.format.DateUtils;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.SearchResultItem;

public class SearchResultPresenter {
	@Nullable
	private SearchResultItem searchResult;

	public SearchResultPresenter(@Nullable SearchResultItem searchResult) {
		this.searchResult = searchResult;
	}

	public CharSequence getFirstAired() {
		if (this.searchResult == null) {
			return "";
		}

		return DateTimeHelper.INSTANCE.getRelativeDate(this.searchResult.getFirstAired(), "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	@StringRes
	public int getIndexerNameRes() {
		if (this.searchResult == null) {
			return 0;
		}

		return this.searchResult.getIndexerNameResource();
	}

	public String getName() {
		if (this.searchResult == null) {
			return "";
		}

		return this.searchResult.getName();
	}
}
