package com.mgaetan89.showsrage.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class BooleanExtensionTest_ToInt {
	@Test
	fun toInt() {
		assertThat(false.toInt()).isEqualTo(0)
		assertThat(true.toInt()).isEqualTo(1)
		assertThat((null as Boolean?).toInt()).isEqualTo(0)
	}
}
