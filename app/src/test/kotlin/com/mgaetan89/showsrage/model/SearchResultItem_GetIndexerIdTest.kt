package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SearchResultItem_GetIndexerIdTest(val searchResultItem: SearchResultItem, val indexerId: Int) {
	@Test
	fun getIndexId() {
		assertThat(this.searchResultItem.getIndexerId()).isEqualTo(this.indexerId)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf(gson.fromJson("{}", SearchResultItem::class.java), 0),
					arrayOf(gson.fromJson("{indexer: -1, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 0),
					arrayOf(gson.fromJson("{indexer: 0, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 0),
					arrayOf(gson.fromJson("{indexer: 1, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 123),
					arrayOf(gson.fromJson("{indexer: 2, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 456),
					arrayOf(gson.fromJson("{indexer: 3, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 789),
					arrayOf(gson.fromJson("{indexer: 4, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 987),
					arrayOf(gson.fromJson("{indexer: 5, tvdbid: 123, tvrageid: 456, tvmazeid: 789, tmdbid: 987}", SearchResultItem::class.java), 0)
			)
		}
	}
}
