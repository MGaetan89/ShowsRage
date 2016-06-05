package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_GetCommandTest {
    @Parameterized.Parameter(1)
    var command: String? = null

    @Parameterized.Parameter(0)
    var shows: List<Show>? = null

    @Test
    fun getCommand() {
        assertThat(ShowsSectionFragment.getCommand(this.shows)).isEqualTo(this.command)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf<Any?>(null, ""),
                    arrayOf<Any?>(emptyList<Any>(), ""),
                    arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java)), "show.stats_123"),
                    arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java), null, gson.fromJson("{indexerid: 456}", Show::class.java)), "show.stats_123|show.stats_456"),
                    arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java), gson.fromJson("{indexerid: 456}", Show::class.java), gson.fromJson("{}", Show::class.java)), "show.stats_123|show.stats_456"),
                    arrayOf<Any?>(listOf(gson.fromJson("{indexerid: 123}", Show::class.java), null, gson.fromJson("{}", Show::class.java)), "show.stats_123")
            )
        }
    }
}
