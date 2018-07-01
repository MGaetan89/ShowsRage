package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.SearchResultItem
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SearchResultPresenterTest(val searchResult: SearchResultItem?, val indexerNameRes: Int, val name: String) {
	private lateinit var presenter: SearchResultPresenter

	@Before
	fun before() {
		this.presenter = SearchResultPresenter(this.searchResult)
	}

	@Test
	fun getIndexerNameRes() {
		assertThat(this.presenter.getIndexerNameRes()).isEqualTo(this.indexerNameRes)
	}

	@Test
	fun getName() {
		assertThat(this.presenter.getName()).isEqualTo(this.name)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			val gson = SickRageApi.gson

			return listOf(
					arrayOf(null, R.string.unknown, ""),
					arrayOf(gson.fromJson("{first_aired: 2015-01-01, indexer: 0, name: \"Show 0\"}", SearchResultItem::class.java), R.string.unknown, "Show 0"),
					arrayOf(gson.fromJson("{first_aired: 2015-01-01, indexer: 1, name: \"Show 1\"}", SearchResultItem::class.java), R.string.the_tvdb, "Show 1"),
					arrayOf(gson.fromJson("{first_aired: 2015-01-01, indexer: 2, name: \"Show 2\"}", SearchResultItem::class.java), R.string.tvrage, "Show 2")
			)
		}
	}
}
