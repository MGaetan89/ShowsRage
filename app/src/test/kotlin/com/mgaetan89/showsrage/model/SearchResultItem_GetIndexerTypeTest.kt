package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SearchResultItem_GetIndexerTypeTest(val searchResult: SearchResultItem, val indexerType: Indexer?) {
    @Test
    fun getIndexerType() {
        assertThat(this.searchResult.getIndexerType()).isEqualTo(this.indexerType)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf<Any?>(gson.fromJson("{}", SearchResultItem::class.java), null),
                    arrayOf<Any?>(gson.fromJson("{indexer: -1}", SearchResultItem::class.java), null),
                    arrayOf<Any?>(gson.fromJson("{indexer: 0}", SearchResultItem::class.java), null),
                    arrayOf<Any?>(gson.fromJson("{indexer: 1}", SearchResultItem::class.java), Indexer.TVDB),
                    arrayOf<Any?>(gson.fromJson("{indexer: 2}", SearchResultItem::class.java), Indexer.TVRAGE),
                    arrayOf<Any?>(gson.fromJson("{indexer: 3}", SearchResultItem::class.java), null)
            )
        }
    }
}
