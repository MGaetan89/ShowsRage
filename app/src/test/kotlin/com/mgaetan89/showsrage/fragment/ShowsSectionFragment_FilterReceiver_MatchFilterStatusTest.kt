package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowsFilters
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_FilterReceiver_MatchFilterStatusTest(val show: Show, val filterStatus: Int, val match: Boolean) {
    @Test
    fun matchFilterStatus() {
        assertThat(ShowsSectionFragment.FilterReceiver.matchFilterStatus(this.show, this.filterStatus)).isEqualTo(this.match)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            val all = ShowsFilters.Status.ALL.status
            val continuing = ShowsFilters.Status.CONTINUING.status
            val ended = ShowsFilters.Status.ENDED.status
            val unknown = ShowsFilters.Status.UNKNOWN.status
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), all, true),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), continuing, true),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), continuing, true),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), continuing, false),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), continuing, false),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), continuing, false),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), continuing, false),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), continuing, false),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), continuing, false),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), continuing or ended, true),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), continuing or ended, true),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), continuing or ended, true),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), continuing or ended, true),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), continuing or ended, false),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), continuing or ended, false),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), continuing or ended, false),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), continuing or ended, false),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), continuing or ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), continuing or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), continuing or unknown, true),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), continuing or unknown, false),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), continuing or unknown, false),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), continuing or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), continuing or unknown, true),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), continuing or unknown, false),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), continuing or unknown, false),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), ended, false),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), ended, false),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), ended, true),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), ended, true),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), ended, false),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), ended, false),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), ended, false),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), ended, false),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), ended or unknown, false),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), ended or unknown, false),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), ended or unknown, true),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), ended or unknown, false),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), ended or unknown, false),
                    arrayOf(gson.fromJson("{status: \"continuing\"}", Show::class.java), unknown, false),
                    arrayOf(gson.fromJson("{status: \"Continuing\"}", Show::class.java), unknown, false),
                    arrayOf(gson.fromJson("{status: \"ended\"}", Show::class.java), unknown, false),
                    arrayOf(gson.fromJson("{status: \"Ended\"}", Show::class.java), unknown, false),
                    arrayOf(gson.fromJson("{status: \"unknown\"}", Show::class.java), unknown, true),
                    arrayOf(gson.fromJson("{status: \"Unknown\"}", Show::class.java), unknown, true),
                    arrayOf(gson.fromJson("{status: \"other status\"}", Show::class.java), unknown, false),
                    arrayOf(gson.fromJson("{status: \"Other Status\"}", Show::class.java), unknown, false)
            )
        }
    }
}
