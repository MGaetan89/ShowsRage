package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.helper.hasText
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class TextHelper_StringHasTextTest(val string: String?, val hasText: Boolean) {
    @Test
    fun stringHasText() {
        assertThat(this.string.hasText()).isEqualTo(this.hasText)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, false),
                    arrayOf<Any?>("", false),
                    arrayOf<Any?>(" ", false),
                    arrayOf<Any?>("n/a", false),
                    arrayOf<Any?>("N/A", false),
                    arrayOf<Any?>("some text", true)
            )
        }
    }
}
