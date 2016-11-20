package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShows
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowsAsyncTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getShows_all() {
        this.realm.getShows(null, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(83)
        })
    }

    @Test
    @UiThreadTest
    fun getShows_animes() {
        this.realm.getShows(true, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(3)
        })
    }

    @Test
    @UiThreadTest
    fun getShows_show() {
        this.realm.getShows(false, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(80)
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
