package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.model.ShowsFilters.Status
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsFilters_Status_IsTest {
    @Parameterized.Parameter(1)
    var all: Boolean = false

    @Parameterized.Parameter(2)
    var continuing: Boolean = false

    @Parameterized.Parameter(3)
    var ended: Boolean = false

    @Parameterized.Parameter(0)
    var status: Int = 0

    @Parameterized.Parameter(4)
    var unknown: Boolean = false

    @Test
    fun isAll() {
        assertThat(ShowsFilters.Status.isAll(this.status)).isEqualTo(this.all)
    }

    @Test
    fun isContinuing() {
        assertThat(ShowsFilters.Status.isContinuing(this.status)).isEqualTo(this.continuing)
    }

    @Test
    fun isEnded() {
        assertThat(ShowsFilters.Status.isEnded(this.status)).isEqualTo(this.ended)
    }

    @Test
    fun isUnknown() {
        assertThat(ShowsFilters.Status.isUnknown(this.status)).isEqualTo(this.unknown)
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
                    arrayOf(0, false, false, false, false),
                    arrayOf(all, true, true, true, true),
                    arrayOf(continuing, false, true, false, false),
                    arrayOf(continuing or ended, false, true, true, false),
                    arrayOf(continuing or ended or unknown, true, true, true, true),
                    arrayOf(continuing or unknown, false, true, false, true),
                    arrayOf(ended, false, false, true, false),
                    arrayOf(ended or unknown, false, false, true, true),
                    arrayOf(unknown, false, false, false, true)
            )
        }
    }
}
