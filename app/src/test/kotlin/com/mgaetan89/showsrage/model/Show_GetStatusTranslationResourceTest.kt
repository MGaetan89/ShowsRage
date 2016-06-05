package com.mgaetan89.showsrage.model

import com.google.gson.Gson
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class Show_GetStatusTranslationResourceTest {
    @Parameterized.Parameter(0)
    var show: Show = Show()

    @Parameterized.Parameter(1)
    var statusTranslationResource: Int = 0

    @Test
    fun getStatusTranslationResource() {
        assertThat(this.show.getStatusTranslationResource()).isEqualTo(this.statusTranslationResource)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(getJsonForStatus(gson, null), 0),
                    arrayOf(getJsonForStatus(gson, ""), 0),
                    arrayOf(getJsonForStatus(gson, "continuing"), R.string.continuing),
                    arrayOf(getJsonForStatus(gson, "Continuing"), R.string.continuing),
                    arrayOf(getJsonForStatus(gson, "ended"), R.string.ended),
                    arrayOf(getJsonForStatus(gson, "Ended"), R.string.ended),
                    arrayOf(getJsonForStatus(gson, "unknown"), R.string.unknown),
                    arrayOf(getJsonForStatus(gson, "Unknown"), R.string.unknown),
                    arrayOf(gson.fromJson("{}", Show::class.java), 0),
                    arrayOf(getJsonForStatus(gson, "status"), 0),
                    arrayOf(getJsonForStatus(gson, "Status"), 0)
            )
        }

        private fun getJsonForStatus(gson: Gson, status: String?): Show {
            return gson.fromJson("{status: \"$status\"}", Show::class.java)
        }
    }
}
