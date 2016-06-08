package com.mgaetan89.showsrage.model

import com.google.gson.Gson
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class History_GetStatusTranslationResourceTest(val history: History, val statusTranslationResource: Int) {
    @Test
    fun getStatusTranslationResource() {
        assertThat(this.history.getStatusTranslationResource()).isEqualTo(this.statusTranslationResource)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(getJsonForStatus(gson, null), 0),
                    arrayOf(getJsonForStatus(gson, ""), 0),
                    arrayOf(getJsonForStatus(gson, "downloaded"), R.string.downloaded),
                    arrayOf(getJsonForStatus(gson, "Downloaded"), R.string.downloaded),
                    arrayOf(getJsonForStatus(gson, "snatched"), R.string.snatched),
                    arrayOf(getJsonForStatus(gson, "Snatched"), R.string.snatched),
                    arrayOf(gson.fromJson("{}", History::class.java), 0),
                    arrayOf(getJsonForStatus(gson, "status"), 0),
                    arrayOf(getJsonForStatus(gson, "Status"), 0)
            )
        }

        private fun getJsonForStatus(gson: Gson, status: String?): History {
            return gson.fromJson("{status: \"$status\"}", History::class.java)
        }
    }
}
