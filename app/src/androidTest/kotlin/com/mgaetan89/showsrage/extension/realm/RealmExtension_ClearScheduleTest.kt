package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.clearSchedule
import com.mgaetan89.showsrage.model.Schedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_ClearScheduleTest : RealmTest() {
    @Before
    fun before() {
        this.realm.isAutoRefresh = false

        assertThat(this.getSchedule()).hasSize(36)
    }

    @Test
    fun clearSchedule() {
        this.realm.clearSchedule()

        assertThat(this.getSchedule()).isEmpty()
    }

    private fun getSchedule() = this.realm.where(Schedule::class.java).findAll()
}
