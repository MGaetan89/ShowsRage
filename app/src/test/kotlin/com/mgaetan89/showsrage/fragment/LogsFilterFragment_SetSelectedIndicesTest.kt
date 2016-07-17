package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogsFilterFragment_SetSelectedIndicesTest(val selectedIndices: MutableSet<Int>, val items: Array<String>, val groups: Array<String>?, val expected: Set<Int>) {
    @Test
    fun getSelectedItems() {
        LogsFilterFragment.setSelectedIndices(this.selectedIndices, this.items, this.groups)

        assertThat(this.selectedIndices).isEqualTo(this.expected)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf(mutableSetOf<Int>(), arrayOf<String>(), null, setOf<Int>()),
                    arrayOf<Any?>(mutableSetOf<Int>(), arrayOf<String>(), arrayOf<String>(), setOf<Int>()),
                    arrayOf<Any?>(mutableSetOf<Int>(), arrayOf<String>(), arrayOf("A", "C"), setOf<Int>()),
                    arrayOf(mutableSetOf<Int>(), arrayOf("A", "B", "C"), null, setOf(0, 1, 2)),
                    arrayOf<Any?>(mutableSetOf<Int>(), arrayOf("A", "B", "C"), arrayOf<String>(), setOf(0, 1, 2)),
                    arrayOf<Any?>(mutableSetOf<Int>(), arrayOf("A", "B", "C"), arrayOf("A", "C"), setOf(0, 2)),
                    arrayOf(mutableSetOf(1, 2, 3, 4), arrayOf<String>(), null, setOf<Int>()),
                    arrayOf<Any?>(mutableSetOf(1, 2, 3, 4), arrayOf<String>(), arrayOf<String>(), setOf<Int>()),
                    arrayOf<Any?>(mutableSetOf(1, 2, 3, 4), arrayOf<String>(), arrayOf("A", "C"), setOf<Int>()),
                    arrayOf(mutableSetOf(1, 2, 3, 4), arrayOf("A", "B", "C"), null, setOf(0, 1, 2)),
                    arrayOf<Any?>(mutableSetOf(1, 2, 3, 4), arrayOf("A", "B", "C"), arrayOf<String>(), setOf(0, 1, 2)),
                    arrayOf<Any?>(mutableSetOf(1, 2, 3, 4), arrayOf("A", "B", "C"), arrayOf("A", "C"), setOf(0, 2))
            )
        }
    }
}
