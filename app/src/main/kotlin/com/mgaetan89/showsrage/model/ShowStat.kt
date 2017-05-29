package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName

data class ShowStat(
		val archived: Int = 0,
		val downloaded: Map<String, Int>? = null,
		val failed: Int = 0,
		val ignored: Int = 0,
		val skipped: Int = 0,
		val snatched: Map<String, Int>? = null,
		@SerializedName("snatched_best") val snatchedBest: Int = 0,
		val subtitled: Int = 0,
		val total: Int = 0,
		val unaired: Int = 0,
		val wanted: Int = 0
) {
	fun getTotalDone(): Int {
		if (this.downloaded == null) {
			return this.archived
		}

		val total = this.downloaded["total"] ?: return this.archived

		return this.archived + total
	}

	fun getTotalPending(): Int {
		if (this.snatched == null) {
			return this.snatchedBest
		}

		val total = this.snatched["total"] ?: return this.snatchedBest

		return this.snatchedBest + total
	}
}
