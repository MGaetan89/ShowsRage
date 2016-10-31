package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShows
import io.realm.Realm
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowsAsyncTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

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

    @After
    fun after() {
        this.realm.isAutoRefresh = false
        this.realm.close()
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
