package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MainActivity_GetInitialMenuIdTest(val action: String?, val menuId: Int) {
    @Test
    fun getInitialMenuId() {
        assertThat(MainActivity.getInitialMenuId(this.action)).isEqualTo(this.menuId)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, R.id.menu_shows),
                    arrayOf<Any?>("", R.id.menu_shows),
                    arrayOf<Any?>("unknown_action", R.id.menu_shows),
                    arrayOf<Any?>(Constants.Intents.ACTION_DISPLAY_HISTORY, R.id.menu_history),
                    arrayOf<Any?>(Constants.Intents.ACTION_DISPLAY_SCHEDULE, R.id.menu_schedule),
                    arrayOf<Any?>(Constants.Intents.ACTION_DISPLAY_SHOW, R.id.menu_shows)
            )
        }
    }
}
