package com.mgaetan89.showsrage.model

import com.google.gson.Gson
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class Episode_GetStatusBackgroundColorTest {
    @Parameterized.Parameter(1)
    var color: Int = 0

    @Parameterized.Parameter(0)
    var episode: Episode = Episode()

    @Test
    fun getStatusBackgroundColor() {
        assertThat(this.episode.getStatusBackgroundColor()).isEqualTo(this.color)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} - {1}")
        fun data(): Collection<Array<Any>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(getJsonForStatus(gson, "archived"), R.color.green),
                    arrayOf(getJsonForStatus(gson, "Archived"), R.color.green),
                    arrayOf(getJsonForStatus(gson, "downloaded"), R.color.green),
                    arrayOf(getJsonForStatus(gson, "Downloaded"), R.color.green),
                    arrayOf(getJsonForStatus(gson, "ignored"), R.color.blue),
                    arrayOf(getJsonForStatus(gson, "Ignored"), R.color.blue),
                    arrayOf(getJsonForStatus(gson, "skipped"), R.color.blue),
                    arrayOf(getJsonForStatus(gson, "Skipped"), R.color.blue),
                    arrayOf(getJsonForStatus(gson, "snatched"), R.color.purple),
                    arrayOf(getJsonForStatus(gson, "snatched (proper)"), R.color.purple),
                    arrayOf(getJsonForStatus(gson, "Snatched"), R.color.purple),
                    arrayOf(getJsonForStatus(gson, "Snatched (proper)"), R.color.purple),
                    arrayOf(getJsonForStatus(gson, "unaired"), R.color.yellow),
                    arrayOf(getJsonForStatus(gson, "Unaired"), R.color.yellow),
                    arrayOf(getJsonForStatus(gson, "wanted"), R.color.red),
                    arrayOf(getJsonForStatus(gson, "Wanted"), R.color.red),
                    arrayOf(gson.fromJson("{}", Episode::class.java), android.R.color.transparent),
                    arrayOf(getJsonForStatus(gson, null), android.R.color.transparent),
                    arrayOf(getJsonForStatus(gson, ""), android.R.color.transparent),
                    arrayOf(getJsonForStatus(gson, "zstatusz"), android.R.color.transparent),
                    arrayOf(getJsonForStatus(gson, "ZStatusZ"), android.R.color.transparent)
            )
        }

        private fun getJsonForStatus(gson: Gson, status: String?): Episode {
            return gson.fromJson("{status: \"$status\"}", Episode::class.java)
        }
    }
}
