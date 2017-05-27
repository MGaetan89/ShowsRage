package com.mgaetan89.showsrage.model

import com.google.gson.Gson
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class Episode_GetStatusTranslationResourceTest(val episode: Episode, val statusTranslationResource: Int) {
	@Test
	fun getStatusTranslationResource() {
		assertThat(this.episode.getStatusTranslationResource()).isEqualTo(this.statusTranslationResource)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters(name = "{index}: {0} - {1}")
		fun data(): Collection<Array<Any>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf(getJsonForStatus(gson, "archived"), R.string.archived),
					arrayOf(getJsonForStatus(gson, "Archived"), R.string.archived),
					arrayOf(getJsonForStatus(gson, "downloaded"), R.string.downloaded),
					arrayOf(getJsonForStatus(gson, "Downloaded"), R.string.downloaded),
					arrayOf(getJsonForStatus(gson, "ignored"), R.string.ignored),
					arrayOf(getJsonForStatus(gson, "Ignored"), R.string.ignored),
					arrayOf(getJsonForStatus(gson, "skipped"), R.string.skipped),
					arrayOf(getJsonForStatus(gson, "Skipped"), R.string.skipped),
					arrayOf(getJsonForStatus(gson, "snatched"), R.string.snatched),
					arrayOf(getJsonForStatus(gson, "Snatched"), R.string.snatched),
					arrayOf(getJsonForStatus(gson, "snatched (proper)"), R.string.snatched_proper),
					arrayOf(getJsonForStatus(gson, "Snatched (proper)"), R.string.snatched_proper),
					arrayOf(getJsonForStatus(gson, "unaired"), R.string.unaired),
					arrayOf(getJsonForStatus(gson, "Unaired"), R.string.unaired),
					arrayOf(getJsonForStatus(gson, "wanted"), R.string.wanted),
					arrayOf(getJsonForStatus(gson, "Wanted"), R.string.wanted),
					arrayOf(gson.fromJson("{}", Episode::class.java), 0),
					arrayOf(getJsonForStatus(gson, null), 0),
					arrayOf(getJsonForStatus(gson, ""), 0),
					arrayOf(getJsonForStatus(gson, "zstatusz"), 0),
					arrayOf(getJsonForStatus(gson, "ZStatusZ"), 0)
			)
		}

		private fun getJsonForStatus(gson: Gson, status: String?): Episode {
			return gson.fromJson("{status: \"$status\"}", Episode::class.java)
		}
	}
}
