package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class ShowsFilters_State_GetViewIdForStateTest(val state: ShowsFilters.State, val viewId: Int) {
    @Test
    fun getViewIdForState() {
        assertThat(ShowsFilters.State.getViewIdForState(this.state)).isEqualTo(this.viewId)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(ShowsFilters.State.ACTIVE, R.id.filter_active),
                    arrayOf(ShowsFilters.State.ALL, R.id.filter_all),
                    arrayOf(ShowsFilters.State.PAUSED, R.id.filter_paused)
            )
        }
    }
}
