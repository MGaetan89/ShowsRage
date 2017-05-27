package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class Show_GetSeasonsListIntTest(val show: Show, val seasonsListInt: List<Int>) {
	@Test
	fun getSeasonsListInt() {
		assertThat(this.show.getSeasonsListInt()).isEqualTo(this.seasonsListInt)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf(gson.fromJson("{}", Show::class.java), emptyList<String>()),
					arrayOf(buildJsonForSeasonList(emptyArray<String>()), emptyList<String>()),
					arrayOf(buildJsonForSeasonList(arrayOf("10")), listOf(10)),
					arrayOf(buildJsonForSeasonList(arrayOf("10", "hello")), listOf(10)),
					arrayOf(buildJsonForSeasonList(arrayOf("10", "hello", "")), listOf(10)),
					arrayOf(buildJsonForSeasonList(arrayOf("10", "hello", "4294967295")), listOf(10)),
					arrayOf(buildJsonForSeasonList(arrayOf("10", "hello", "4294967295", "42")), listOf(10, 42))
			)
		}

		private fun buildJsonForSeasonList(seasonList: Array<String>?): Show {
			return Show().apply {
				this.seasonList = RealmList<RealmString>().apply {
					this.addAll(seasonList?.map(::RealmString) ?: emptyList())
				}
			}
		}
	}
}
