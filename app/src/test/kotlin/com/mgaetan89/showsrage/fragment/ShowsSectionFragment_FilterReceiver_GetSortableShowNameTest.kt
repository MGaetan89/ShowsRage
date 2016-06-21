package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_FilterReceiver_GetSortableShowNameTest(val show: Show, val ignoreArticles: Boolean, val showName: String?) {
    @Test
    fun matchFilterState() {
        assertThat(ShowsSectionFragment.FilterReceiver.getSortableShowName(this.show, this.ignoreArticles)).isEqualTo(this.showName)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(gson.fromJson("{}", Show::class.java), false, ""),
                    arrayOf(gson.fromJson("{show_name: null}", Show::class.java), false, null),
                    arrayOf(gson.fromJson("{show_name: \"\"}", Show::class.java), false, ""),
                    arrayOf(gson.fromJson("{show_name: \"a random show\"}", Show::class.java), false, "a random show"),
                    arrayOf(gson.fromJson("{show_name: \"A Random Show\"}", Show::class.java), false, "A Random Show"),
                    arrayOf(gson.fromJson("{show_name: \"an epic random show\"}", Show::class.java), false, "an epic random show"),
                    arrayOf(gson.fromJson("{show_name: \"An Epic Random Show\"}", Show::class.java), false, "An Epic Random Show"),
                    arrayOf(gson.fromJson("{show_name: \"arrow\"}", Show::class.java), false, "arrow"),
                    arrayOf(gson.fromJson("{show_name: \"Arrow\"}", Show::class.java), false, "Arrow"),
                    arrayOf(gson.fromJson("{show_name: \"breaking bad\"}", Show::class.java), false, "breaking bad"),
                    arrayOf(gson.fromJson("{show_name: \"Breaking Bad\"}", Show::class.java), false, "Breaking Bad"),
                    arrayOf(gson.fromJson("{show_name: \"fear the walking dead\"}", Show::class.java), false, "fear the walking dead"),
                    arrayOf(gson.fromJson("{show_name: \"Fear the Walking Dead\"}", Show::class.java), false, "Fear the Walking Dead"),
                    arrayOf(gson.fromJson("{show_name: \"the a list\"}", Show::class.java), false, "the a list"),
                    arrayOf(gson.fromJson("{show_name: \"The A List\"}", Show::class.java), false, "The A List"),
                    arrayOf(gson.fromJson("{show_name: \"the flash\"}", Show::class.java), false, "the flash"),
                    arrayOf(gson.fromJson("{show_name: \"The Flash\"}", Show::class.java), false, "The Flash"),
                    arrayOf(gson.fromJson("{}", Show::class.java), true, ""),
                    arrayOf(gson.fromJson("{show_name: null}", Show::class.java), true, null),
                    arrayOf(gson.fromJson("{show_name: \"\"}", Show::class.java), true, ""),
                    arrayOf(gson.fromJson("{show_name: \"a random show\"}", Show::class.java), true, "random show"),
                    arrayOf(gson.fromJson("{show_name: \"A Random Show\"}", Show::class.java), true, "Random Show"),
                    arrayOf(gson.fromJson("{show_name: \"an epic random show\"}", Show::class.java), true, "epic random show"),
                    arrayOf(gson.fromJson("{show_name: \"An Epic Random Show\"}", Show::class.java), true, "Epic Random Show"),
                    arrayOf(gson.fromJson("{show_name: \"arrow\"}", Show::class.java), true, "arrow"),
                    arrayOf(gson.fromJson("{show_name: \"Arrow\"}", Show::class.java), true, "Arrow"),
                    arrayOf(gson.fromJson("{show_name: \"breaking bad\"}", Show::class.java), true, "breaking bad"),
                    arrayOf(gson.fromJson("{show_name: \"Breaking Bad\"}", Show::class.java), true, "Breaking Bad"),
                    arrayOf(gson.fromJson("{show_name: \"fear the walking dead\"}", Show::class.java), true, "fear the walking dead"),
                    arrayOf(gson.fromJson("{show_name: \"Fear the Walking Dead\"}", Show::class.java), true, "Fear the Walking Dead"),
                    arrayOf(gson.fromJson("{show_name: \"the a list\"}", Show::class.java), true, "a list"),
                    arrayOf(gson.fromJson("{show_name: \"The A List\"}", Show::class.java), true, "A List"),
                    arrayOf(gson.fromJson("{show_name: \"the flash\"}", Show::class.java), true, "flash"),
                    arrayOf(gson.fromJson("{show_name: \"The Flash\"}", Show::class.java), true, "Flash")
            )
        }
    }
}
