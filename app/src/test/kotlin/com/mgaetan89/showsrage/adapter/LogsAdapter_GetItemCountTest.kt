package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.LogEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogsAdapter_GetItemCountTest(val logs: List<LogEntry>, val itemCount: Int) {
    private lateinit var adapter: LogsAdapter

    @Before
    fun before() {
        this.adapter = LogsAdapter(this.logs)
    }

    @Test
    fun getItemCount() {
        assertThat(this.adapter.itemCount).isEqualTo(this.itemCount)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(emptyList<Any>(), 0),
                    arrayOf(listOf(LogEntry()), 1),
                    arrayOf(listOf(LogEntry(), LogEntry(), LogEntry()), 3)
            )
        }
    }
}
