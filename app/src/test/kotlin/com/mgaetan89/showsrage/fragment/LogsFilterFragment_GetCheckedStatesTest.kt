package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogsFilterFragment_GetCheckedStatesTest(val size: Int, val selectedIndices: Set<Int>, val checkedStates: BooleanArray) {
	@Test
	fun getCheckedStates() {
		assertThat(LogsFilterFragment.getCheckedStates(this.size, this.selectedIndices)).isEqualTo(this.checkedStates)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any>> {
			return listOf(
					arrayOf(0, setOf<Int>(), booleanArrayOf()),
					arrayOf(0, setOf(1, 3, 5), booleanArrayOf()),
					arrayOf(5, setOf<Int>(), booleanArrayOf(false, false, false, false, false)),
					arrayOf(5, setOf(1, 3, 5), booleanArrayOf(false, true, false, true, false))
			)
		}
	}
}
