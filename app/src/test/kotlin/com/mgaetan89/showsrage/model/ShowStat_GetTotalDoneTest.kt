package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowStat_GetTotalDoneTest(val showStat: ShowStat, val totalDone: Int) {
	@Test
	fun getStatusBackgroundColor() {
		assertThat(this.showStat.getTotalDone()).isEqualTo(this.totalDone)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters(name = "{index}: {0} - {1}")
		fun data(): Collection<Array<Any>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf(ShowStat(), 0),
					arrayOf(gson.fromJson("{}", ShowStat::class.java), 0),
					arrayOf(gson.fromJson("{archived: 0}", ShowStat::class.java), 0),
					arrayOf(gson.fromJson("{archived: 1}", ShowStat::class.java), 1),
					arrayOf(gson.fromJson("{archived: 2, downloaded: null}", ShowStat::class.java), 2),
					arrayOf(gson.fromJson("{archived: 3, downloaded: {}}", ShowStat::class.java), 3),
					arrayOf(gson.fromJson("{archived: 4, downloaded: {1080p_bluray: 1}}", ShowStat::class.java), 4),
					arrayOf(gson.fromJson("{archived: 5, downloaded: {total: 2}}", ShowStat::class.java), 7),
					arrayOf(gson.fromJson("{archived: 6, downloaded: {1080p_bluray: 3, total: 4}}", ShowStat::class.java), 10)
			)
		}
	}
}
