package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getSchedule
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetScheduleTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getSchedule() {
        this.realm.getSchedule("soon", RealmChangeListener {
            it.removeChangeListeners()

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
            it.removeChangeListeners()

            assertThat(it).isEmpty()
        })
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Looper.myLooper().quit()
        }
    }
}
