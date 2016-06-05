package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class Episode_GetStatusForMenuIdTest {
    @Parameterized.Parameter(1)
    var episodeStatus: String? = null

    @Parameterized.Parameter(0)
    var menuId: Int = 0

    @Test
    fun getStatusForMenuId() {
        assertThat(Episode.getStatusForMenuId(this.menuId)).isEqualTo(this.episodeStatus)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(R.id.menu_episode_set_status_failed, "failed"),
                    arrayOf<Any?>(R.id.menu_episode_set_status_ignored, "ignored"),
                    arrayOf<Any?>(R.id.menu_episode_set_status_skipped, "skipped"),
                    arrayOf<Any?>(R.id.menu_episode_set_status_wanted, "wanted"),
                    arrayOf<Any?>(R.id.menu_change_quality, null)
            )
        }
    }
}
