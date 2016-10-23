package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.buildComparator
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.Show
import io.realm.Realm
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowsAsyncTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())
    }

    @Test
    @UiThreadTest
    fun getShows_all() {
        this.realm.getShows(null, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(83)
            assertThat(it).isSortedAccordingTo(buildComparator(Show::showName))
        })
    }

    @Test
    @UiThreadTest
    fun getShows_animes() {
        this.realm.getShows(true, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(3)
            assertThat(it).isSortedAccordingTo(buildComparator(Show::showName))
        })
    }

    @Test
    @UiThreadTest
    fun getShows_show() {
        this.realm.getShows(false, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(80)
            assertThat(it).isSortedAccordingTo(buildComparator(Show::showName))
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
