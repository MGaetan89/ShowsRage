package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.assertj.core.data.MapEntry
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_GetCommandParametersTest(val shows: List<Show>?, val commandParameters: Array<MapEntry>) {
	@Test
	fun getCommandParameters() {
		assertThat(ShowsSectionFragment.getCommandParameters(this.shows)).containsOnly(*this.commandParameters)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf<Any?>(null, arrayOfNulls<MapEntry>(0)),
					arrayOf<Any?>(emptyList<Any>(), arrayOfNulls<MapEntry>(0)),
					arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java)), arrayOf(entry("show.stats_123.indexerid", 123))),
					arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java), null, gson.fromJson("{indexerid: 456}", Show::class.java)), arrayOf(entry("show.stats_123.indexerid", 123), entry("show.stats_456.indexerid", 456))),
					arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java), gson.fromJson("{indexerid: 456}", Show::class.java), gson.fromJson("{}", Show::class.java)), arrayOf(entry("show.stats_123.indexerid", 123), entry("show.stats_456.indexerid", 456))),
					arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java), null, gson.fromJson("{}", Show::class.java)), arrayOf(entry("show.stats_123.indexerid", 123)))
			)
		}
	}
}
