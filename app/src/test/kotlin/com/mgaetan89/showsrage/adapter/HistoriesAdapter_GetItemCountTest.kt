package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.History
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class HistoriesAdapter_GetItemCountTest(val histories: List<History>, val itemCount: Int) {
    private lateinit var adapter: HistoriesAdapter

    @Before
    fun before() {
        this.adapter = HistoriesAdapter(this.histories)
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
                    arrayOf(listOf(History()), 1),
                    arrayOf(listOf(History(), History(), History()), 3)
            )
        }
    }
}
