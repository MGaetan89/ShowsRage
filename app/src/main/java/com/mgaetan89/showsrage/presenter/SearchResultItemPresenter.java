package com.mgaetan89.showsrage.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.mgaetan89.showsrage.helper.DateTimeHelper;
import com.mgaetan89.showsrage.model.SearchResultItem;

public class SearchResultItemPresenter {
	@Nullable
	private Context context = null;

	@Nullable
	private SearchResultItem searchResultItem = null;

	public SearchResultItemPresenter(@Nullable SearchResultItem searchResultItem, @Nullable Context context) {
		this.context = context;
		this.searchResultItem = searchResultItem;
	}

	public CharSequence getFirstAirDate() {
		if (this.searchResultItem == null) {
			return "";
		}

		return DateTimeHelper.getRelativeDate(this.searchResultItem.getFirstAired(), "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS);
	}

	public String getIndexer() {
		if (this.searchResultItem == null) {
			return "";
		}

		if (this.context != null) {
			return this.context.getString(this.searchResultItem.getIndexerNameResource());
		}

		return "";
	}

	public String getShowName() {
		if (this.searchResultItem == null) {
			return "";
		}

		return this.searchResultItem.getName();
	}
}
