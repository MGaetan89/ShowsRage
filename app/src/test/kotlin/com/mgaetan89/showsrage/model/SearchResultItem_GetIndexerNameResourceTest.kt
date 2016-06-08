package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SearchResultItem_GetIndexerNameResourceTest(val searchResult: SearchResultItem, val indexerNameResource: Int) {
    @Test
    fun getIndexerName() {
        assertThat(this.searchResult.getIndexerNameResource()).isEqualTo(this.indexerNameResource)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(gson.fromJson("{}", SearchResultItem::class.java), 0),
                    arrayOf(gson.fromJson("{indexer: -1}", SearchResultItem::class.java), 0),
                    arrayOf(gson.fromJson("{indexer: 0}", SearchResultItem::class.java), 0),
                    arrayOf(gson.fromJson("{indexer: 1}", SearchResultItem::class.java), R.string.the_tvdb),
                    arrayOf(gson.fromJson("{indexer: 2}", SearchResultItem::class.java), R.string.tvrage),
                    arrayOf(gson.fromJson("{indexer: 3}", SearchResultItem::class.java), 0)
            )
        }
    }
}
