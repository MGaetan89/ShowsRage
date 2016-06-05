package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.model.ShowsFilters.Status
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsFilters_Status_GetStatusForStatesTest {
    @Parameterized.Parameter(0)
    var all: Boolean = false

    @Parameterized.Parameter(1)
    var continuing: Boolean = false

    @Parameterized.Parameter(2)
    var ended: Boolean = false

    @Parameterized.Parameter(4)
    var status: Int = 0

    @Parameterized.Parameter(3)
    var unknown: Boolean = false

    @Test
    fun getStatusForStates() {
        assertThat(ShowsFilters.Status.getStatusForStates(this.all, this.continuing, this.ended, this.unknown)).isEqualTo(this.status)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            val all = Status.ALL.status
            val continuing = Status.CONTINUING.status
            val ended = Status.ENDED.status
            val unknown = Status.UNKNOWN.status

            return listOf(
                    arrayOf(false, false, false, false, all),
                    arrayOf(false, false, false, true, unknown),
                    arrayOf(false, false, true, false, ended),
                    arrayOf(false, false, true, true, ended or unknown),
                    arrayOf(false, true, false, false, continuing),
                    arrayOf(false, true, false, true, continuing or unknown),
                    arrayOf(false, true, true, false, continuing or ended),
                    arrayOf(false, true, true, true, continuing or ended or unknown),
                    arrayOf(true, false, false, false, all),
                    arrayOf(true, false, false, true, all),
                    arrayOf(true, false, true, false, all),
                    arrayOf(true, false, true, true, all),
                    arrayOf(true, true, false, false, all),
                    arrayOf(true, true, false, true, all),
                    arrayOf(true, true, true, false, all),
                    arrayOf(true, true, true, true, all)
            )
        }
    }
}
