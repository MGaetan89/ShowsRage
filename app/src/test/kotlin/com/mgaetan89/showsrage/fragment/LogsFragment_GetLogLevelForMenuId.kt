package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogsFragment_GetLogLevelForMenuId {
    @Parameterized.Parameter(1)
    var logLevel: LogLevel? = null

    @Parameterized.Parameter(0)
    var menuId: Int = 0

    @Test
    fun getLogLevelForMenuId() {
        assertThat(LogsFragment.getLogLevelForMenuId(this.menuId)).isEqualTo(this.logLevel)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(0, null),
                    arrayOf<Any?>(R.id.menu_debug, LogLevel.DEBUG),
                    arrayOf<Any?>(R.id.menu_error, LogLevel.ERROR),
                    arrayOf<Any?>(R.id.menu_info, LogLevel.INFO),
                    arrayOf<Any?>(R.id.menu_warning, LogLevel.WARNING),
                    arrayOf<Any?>(R.id.menu_change_quality, null)
            )
        }
    }
}
