package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_FilterReceiver_MatchFilterStateTest(val show: Show, val filterState: ShowsFilters.State?, val match: Boolean) {
    @Test
    fun matchFilterState() {
        assertThat(ShowsSectionFragment.FilterReceiver.matchFilterState(this.show, this.filterState)).isEqualTo(this.match)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(gson.fromJson("{paused: 0}", Show::class.java), null, false),
                    arrayOf(gson.fromJson("{paused: 1}", Show::class.java), null, false),
                    arrayOf<Any?>(gson.fromJson("{paused: 0}", Show::class.java), ShowsFilters.State.ACTIVE, true),
                    arrayOf<Any?>(gson.fromJson("{paused: 1}", Show::class.java), ShowsFilters.State.ACTIVE, false),
                    arrayOf<Any?>(gson.fromJson("{paused: 0}", Show::class.java), ShowsFilters.State.ALL, true),
                    arrayOf<Any?>(gson.fromJson("{paused: 1}", Show::class.java), ShowsFilters.State.ALL, true),
                    arrayOf<Any?>(gson.fromJson("{paused: 0}", Show::class.java), ShowsFilters.State.PAUSED, false),
                    arrayOf<Any?>(gson.fromJson("{paused: 1}", Show::class.java), ShowsFilters.State.PAUSED, true)
            )
        }
    }
}
