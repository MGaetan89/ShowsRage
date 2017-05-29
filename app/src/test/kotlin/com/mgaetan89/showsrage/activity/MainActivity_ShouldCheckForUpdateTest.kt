package com.mgaetan89.showsrage.activity

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MainActivity_ShouldCheckForUpdateTest(val checkInterval: Long, val manualCheck: Boolean, val lastCheckTime: Long, val result: Boolean) {
	@Test
	fun shouldCheckForUpdate() {
		assertThat(MainActivity.shouldCheckForUpdate(this.checkInterval, this.manualCheck, this.lastCheckTime)).isEqualTo(this.result)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any>> {
			val checkInterval = 21600000L
			val currentTime = System.currentTimeMillis()
			val offset = 5000L

			return listOf(
					// Automatic check
					arrayOf(0L, false, 0L, false),
					arrayOf(0L, false, currentTime - offset, false),
					arrayOf(0L, false, currentTime, false),
					arrayOf(0L, false, currentTime + offset, false),
					arrayOf(checkInterval, false, 0L, true),
					arrayOf(checkInterval, false, currentTime - checkInterval - offset, true),
					arrayOf(checkInterval, false, currentTime - checkInterval, true),
					arrayOf(checkInterval, false, currentTime - checkInterval + offset, false),

					// Manual check
					arrayOf(0L, true, 0, true),
					arrayOf(0L, true, currentTime - offset, true),
					arrayOf(0L, true, currentTime, true),
					arrayOf(0L, true, currentTime + offset, true),
					arrayOf(checkInterval, true, 0L, true),
					arrayOf(checkInterval, true, currentTime - checkInterval - offset, true),
					arrayOf(checkInterval, true, currentTime - checkInterval, true),
					arrayOf(checkInterval, true, currentTime - checkInterval + offset, true)
			)
		}
	}
}
