package com.mgaetan89.showsrage.helper

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class TextHelper_StringHumanizeTest(val string: String, val output: String) {
    @Test
    fun stringHumanize() {
        assertThat(this.string.humanize()).isEqualTo(this.output)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>("", ""),
                    arrayOf<Any?>("a", "A"),
                    arrayOf<Any?>("A", "A"),
                    arrayOf<Any?>("hello", "Hello"),
                    arrayOf<Any?>("Hello", "Hello"),
                    arrayOf<Any?>("HELLO", "Hello"),
                    arrayOf<Any?>("hello world", "Hello World"),
                    arrayOf<Any?>("HELLO WORLD", "Hello World"),
                    arrayOf<Any?>("Hello World", "Hello World")
            )
        }
    }
}
