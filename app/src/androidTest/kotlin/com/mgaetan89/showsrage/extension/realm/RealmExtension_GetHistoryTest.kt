package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.buildComparator
import com.mgaetan89.showsrage.extension.getHistory
import com.mgaetan89.showsrage.model.History
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetHistoryTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getHistory() {
        this.realm.getHistory(RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(100)
            assertThat(it).isSortedAccordingTo(buildComparator(History::date, true))
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
