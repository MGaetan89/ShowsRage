package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.Locale

@RunWith(Parameterized::class)
class EpisodeDetailFragment_GetDisplayableSubtitlesLanguagesTest(val subtitles: String, val result: String) {
    @Before
    fun before() {
        Locale.setDefault(Locale.ENGLISH)
    }

    @Test
    fun getDisplayableSubtitlesLanguages() {
        assertThat(EpisodeDetailFragment.getDisplayableSubtitlesLanguages(this.subtitles)).isEqualTo(this.result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf<Any>("", ""),
                    arrayOf<Any>("ge", "German"),
                    arrayOf<Any>("eng", "English"),
                    arrayOf<Any>("french", "French"),
                    arrayOf<Any>("rus,spa", "Russian, Spanish"),
                    arrayOf<Any>("ch,,ita", "Chinese, Italian")
            )
        }
    }
}
