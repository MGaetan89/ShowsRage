package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogsFilterFragment_GetSelectedItemsTest(val items: Array<String>, val selectedIndices: Set<Int>, val selectedItems: Array<String>) {
	@Test
	fun getSelectedItems() {
		assertThat(LogsFilterFragment.getSelectedItems(this.items, this.selectedIndices)).isEqualTo(this.selectedItems)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any>> {
			return listOf(
					arrayOf(arrayOf<String>(), setOf<Int>(), arrayOf<String>()),
					arrayOf(arrayOf<String>(), setOf(1, 3, 5), arrayOf<String>()),
					arrayOf(arrayOf("A", "B", "C"), setOf(1, 3, 5), arrayOf("B")),
					arrayOf(arrayOf("A", "B", "C"), setOf<Int>(), arrayOf<String>())
			)
		}
	}
}
