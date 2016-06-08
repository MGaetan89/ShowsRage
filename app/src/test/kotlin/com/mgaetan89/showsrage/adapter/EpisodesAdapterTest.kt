package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.Episode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class EpisodesAdapterTest(val episodes: List<Episode>, val itemCount: Int, val position: Int, val episodeNumber: Int) {
    private lateinit var adapter: EpisodesAdapter

    @Before
    fun before() {
        this.adapter = EpisodesAdapter(this.episodes, 1, 0, false)
    }

    @Test
    fun getEpisodeNumber() {
        assertThat(this.adapter.getEpisodeNumber(this.position)).isEqualTo(this.episodeNumber)
    }

    @Test
    fun getItemCount() {
        assertThat(this.adapter.itemCount).isEqualTo(this.itemCount)
    }

    @Test
    fun isReversed() {
        assertThat(this.adapter.reversed).isFalse()
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(emptyList<Any>(), 0, 0, 1),
                    arrayOf(emptyList<Any>(), 0, 1, 2),
                    arrayOf(listOf(Episode()), 1, 0, 1),
                    arrayOf(listOf(Episode()), 1, 1, 2),
                    arrayOf(listOf(Episode(), Episode(), Episode()), 3, 0, 1),
                    arrayOf(listOf(Episode(), Episode(), Episode()), 3, 1, 2),
                    arrayOf(listOf(Episode(), Episode(), Episode()), 3, 2, 3),
                    arrayOf(listOf(Episode(), Episode(), Episode()), 3, 3, 4)
            )
        }
    }
}
