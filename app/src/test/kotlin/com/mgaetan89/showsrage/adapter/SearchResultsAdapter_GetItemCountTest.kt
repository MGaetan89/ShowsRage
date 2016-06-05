package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.SearchResultItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SearchResultsAdapter_GetItemCountTest {
    @Parameterized.Parameter(1)
    var itemCount: Int = 0

    @Parameterized.Parameter(0)
    var searchResultItems: List<SearchResultItem> = emptyList()

    private lateinit var adapter: SearchResultsAdapter

    @Before
    fun before() {
        this.adapter = SearchResultsAdapter(this.searchResultItems)
    }

    @Test
    fun getItemCount() {
        assertThat(this.adapter.itemCount).isEqualTo(this.itemCount)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(emptyList<Any>(), 0),
                    arrayOf(listOf(SearchResultItem()), 1),
                    arrayOf(listOf(SearchResultItem(), SearchResultItem(), SearchResultItem()), 3)
            )
        }
    }
}
