package com.mgaetan89.showsrage.helper

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

@RunWith(Parameterized::class)
class TextHelper_StringToLocaleTest(val string: String, val locale: Locale?) {
    @Test
    fun stringToLocale() {
        val locale = this.string.toLocale()

        if (this.locale == null) {
            assertThat(locale).isNull()
        } else {
            assertThat(locale!!.displayLanguage).isEqualTo(this.locale.displayLanguage)
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>("", null),
                    arrayOf<Any?>("dumb", null),
                    arrayOf<Any?>("ge", Locale.GERMAN),
                    arrayOf<Any?>("eng", Locale.ENGLISH),
                    arrayOf<Any?>("french", Locale.FRENCH)
            )
        }
    }
}
