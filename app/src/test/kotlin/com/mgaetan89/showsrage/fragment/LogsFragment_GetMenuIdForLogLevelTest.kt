package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogsFragment_GetMenuIdForLogLevelTest(val logLevel: LogLevel?, val menuId: Int) {
    @Test
    fun getMenuIdForLogLevel() {
        assertThat(LogsFragment.getMenuIdForLogLevel(this.logLevel)).isEqualTo(this.menuId)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, 0),
                    arrayOf<Any?>(LogLevel.DEBUG, R.id.menu_debug),
                    arrayOf<Any?>(LogLevel.ERROR, R.id.menu_error),
                    arrayOf<Any?>(LogLevel.INFO, R.id.menu_info),
                    arrayOf<Any?>(LogLevel.WARNING, R.id.menu_warning)
            )
        }
    }
}
