package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RemoteControlFragment_FormatTimeTest(val time: Long, val formattedTime: String) {
    @Test
    fun getQueryFromIntent() {
        assertThat(RemoteControlFragment.formatTime(this.time)).isEqualTo(this.formattedTime)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(-42L, "00:00"),
                    arrayOf(0L, "00:00"),
                    arrayOf(1000L, "00:01"),
                    arrayOf(5000L, "00:05"),
                    arrayOf(15000L, "00:15"),
                    arrayOf(30000L, "00:30"),
                    arrayOf(45000L, "00:45"),
                    arrayOf(60000L, "01:00"),
                    arrayOf(75000L, "01:15"),
                    arrayOf(90000L, "01:30"),
                    arrayOf(105000L, "01:45"),
                    arrayOf(120000L, "02:00"),
                    arrayOf(3600000L, "01:00:00"),
                    arrayOf(3660000L, "01:01:00"),
                    arrayOf(3661000L, "01:01:01")
            )
        }
    }
}
