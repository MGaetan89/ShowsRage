package com.mgaetan89.showsrage.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class IndexerTest {
    @Test
    fun paramName() {
        assertThat(Indexer.TMDB.paramName).isEqualTo("tmdbid")
        assertThat(Indexer.TVDB.paramName).isEqualTo("tvdbid")
        assertThat(Indexer.TVMAZE.paramName).isEqualTo("tvmazeid")
        assertThat(Indexer.TVRAGE.paramName).isEqualTo("tvrageid")
    }
}
