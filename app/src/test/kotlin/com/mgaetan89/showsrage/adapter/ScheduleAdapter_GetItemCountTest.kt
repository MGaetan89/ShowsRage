package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.Schedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ScheduleAdapter_GetItemCountTest(val schedules: List<Schedule>, val itemCount: Int) {
    private lateinit var adapter: ScheduleAdapter

    @Before
    fun before() {
        this.adapter = ScheduleAdapter(this.schedules)
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
                    arrayOf(listOf(Schedule()), 1),
                    arrayOf(listOf(Schedule(), Schedule(), Schedule()), 3)
            )
        }
    }
}
