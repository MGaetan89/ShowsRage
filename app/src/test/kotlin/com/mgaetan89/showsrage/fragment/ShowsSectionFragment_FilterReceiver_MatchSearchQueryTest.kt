package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsSectionFragment_FilterReceiver_MatchSearchQueryTest {
    @Parameterized.Parameter(1)
    var searchQuery: String? = null

    @Parameterized.Parameter(2)
    var match: Boolean = false

    @Parameterized.Parameter(0)
    var show: Show = Show()

    @Test
    fun matchSearchQuery() {
        assertThat(ShowsSectionFragment.FilterReceiver.matchSearchQuery(this.show, this.searchQuery)).isEqualTo(this.match)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(gson.fromJson("{show_name: \"\"}", Show::class.java), null, true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"\"}", Show::class.java), "", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"\"}", Show::class.java), " ", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"\"}", Show::class.java), "  ", true),
                    arrayOf(gson.fromJson("{show_name: \"Show Name 1\"}", Show::class.java), null, true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 2\"}", Show::class.java), "", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 3\"}", Show::class.java), " ", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 4\"}", Show::class.java), "  ", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"\"}", Show::class.java), "abc", false),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"\"}", Show::class.java), "Abc", false),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 5\"}", Show::class.java), "Show", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 6\"}", Show::class.java), "show", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 7\"}", Show::class.java), "Name", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 8\"}", Show::class.java), "name", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 9\"}", Show::class.java), "Show name", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 10\"}", Show::class.java), "show Name", true),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 11\"}", Show::class.java), "Abc", false),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 12\"}", Show::class.java), "abc", false),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 13\"}", Show::class.java), "Name Show", false),
                    arrayOf<Any?>(gson.fromJson("{show_name: \"Show Name 14\"}", Show::class.java), "name show", false)
            )
        }
    }
}
