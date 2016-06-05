package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsFilters_State_GetStateForViewIdTest {
    @Parameterized.Parameter(1)
    var state: ShowsFilters.State = ShowsFilters.State.ALL

    @Parameterized.Parameter(0)
    var viewId: Int = 0

    @Test
    fun getStateForViewId() {
        assertThat(ShowsFilters.State.getStateForViewId(this.viewId)).isEqualTo(this.state)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(-42, ShowsFilters.State.ALL),
                    arrayOf(0, ShowsFilters.State.ALL),
                    arrayOf(42, ShowsFilters.State.ALL),
                    arrayOf(R.id.filter_status_ended, ShowsFilters.State.ALL),
                    arrayOf(R.id.filter_active, ShowsFilters.State.ACTIVE),
                    arrayOf(R.id.filter_all, ShowsFilters.State.ALL),
                    arrayOf(R.id.filter_paused, ShowsFilters.State.PAUSED)
            )
        }
    }
}
