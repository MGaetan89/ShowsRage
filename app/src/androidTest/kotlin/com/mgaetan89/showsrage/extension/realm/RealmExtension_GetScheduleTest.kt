package com.mgaetan89.showsrage.extension.realm

import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getSchedule
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetScheduleTest : RealmTest() {
    @Before
    fun before() {
        this.realm.isAutoRefresh = true
    }

    @Test
    @UiThreadTest
    fun getSchedule() {
        this.realm.getSchedule("soon", RealmChangeListener {
            it.removeAllChangeListeners()

            assertThat(it).hasSize(8)

            for (i in 1 until it.size) {
                assertThat(it[i].airDate > it [i - 1].airDate).isTrue()
            }
        })
    }

    @Test
    @UiThreadTest
    fun getSchedule_notFound() {
        this.realm.getSchedule("Monday", RealmChangeListener {
            it.removeAllChangeListeners()

            assertThat(it).isEmpty()
        })
    }
}
