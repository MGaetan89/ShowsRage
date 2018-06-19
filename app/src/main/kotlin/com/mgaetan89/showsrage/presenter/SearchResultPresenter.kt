package com.mgaetan89.showsrage.presenter

import android.support.annotation.StringRes
import android.text.format.DateUtils
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.SearchResultItem

class SearchResultPresenter(private val searchResult: SearchResultItem?) {
	fun getFirstAired(): CharSequence? {
		if (this.searchResult == null) {
			return ""
		}

		return DateTimeHelper.getRelativeDate(this.searchResult.firstAired, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
	}

	@StringRes
	fun getIndexerNameRes() = this.searchResult?.getIndexerNameResource() ?: R.string.unknown

	fun getName() = this.searchResult?.name ?: ""
}
