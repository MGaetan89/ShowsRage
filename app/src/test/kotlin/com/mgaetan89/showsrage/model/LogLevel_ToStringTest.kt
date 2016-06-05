package com.mgaetan89.showsrage.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogLevel_ToStringTest {
    @Parameterized.Parameter(0)
    var logLevel: LogLevel = LogLevel.DEBUG

    @Parameterized.Parameter(1)
    var result: String = ""

    @Test
    fun toStringTest() {
        assertThat(this.logLevel.toString()).isEqualTo(this.result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(LogLevel.DEBUG, "debug"),
                    arrayOf(LogLevel.ERROR, "error"),
                    arrayOf(LogLevel.INFO, "info"),
                    arrayOf(LogLevel.WARNING, "warning")
            )
        }
    }
}
