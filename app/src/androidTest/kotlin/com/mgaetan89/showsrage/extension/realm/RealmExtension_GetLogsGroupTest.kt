package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getLogsGroup
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetLogsGroupTest : RealmTest() {
    @Test
    fun getLogsGroup() {
        val logsGroup = this.realm.getLogsGroup()

        assertThat(logsGroup).hasSize(4)
        assertThat(logsGroup).containsExactly("POSTPROCESSER", "SEARCHQUEUE-BACKLOG-75897", "SEARCHQUEUE-DAILY-SEARCH", "TORNADO")
    }
}
