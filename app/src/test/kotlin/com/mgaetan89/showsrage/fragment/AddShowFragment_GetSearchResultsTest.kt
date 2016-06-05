package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.SearchResults
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AddShowFragment_GetSearchResultsTest {
    @Parameterized.Parameter(0)
    var searchResults: SearchResults? = null

    @Parameterized.Parameter(1)
    var size: Int = 0

    @Test
    fun getSearchResults() {
        assertThat(AddShowFragment.getSearchResults(this.searchResults).size).isEqualTo(this.size)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf<Any?>(null, 0),
                    arrayOf<Any?>(SearchResults(), 0),
                    arrayOf<Any?>(gson.fromJson("{data: null}", SearchResults::class.java), 0),
                    arrayOf<Any?>(gson.fromJson("{data: {}}", SearchResults::class.java), 0),
                    arrayOf<Any?>(gson.fromJson("{data: {results: null}}", SearchResults::class.java), 0),
                    arrayOf<Any?>(gson.fromJson("{data: {results: []}}", SearchResults::class.java), 0),
                    arrayOf<Any?>(gson.fromJson("{data: {results: [{}, {}]}}", SearchResults::class.java), 2)
            )
        }
    }
}
