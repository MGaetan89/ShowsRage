package com.mgaetan89.showsrage.presenter

import android.support.annotation.StringRes
import android.text.format.DateUtils
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.toRelativeDate
import com.mgaetan89.showsrage.model.SearchResultItem

class SearchResultPresenter(private val searchResult: SearchResultItem?) {
	fun getFirstAired(): CharSequence? {
		if (this.searchResult == null) {
			return ""
		}

		return this.searchResult.firstAired.toRelativeDate("yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
	}

	@StringRes
	fun getIndexerNameRes() = this.searchResult?.getIndexerNameResource() ?: R.string.unknown

	fun getName() = this.searchResult?.name ?: ""
}
