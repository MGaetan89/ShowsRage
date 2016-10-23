package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getSchedule
import com.mgaetan89.showsrage.initRealm
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
class RealmExtension_GetScheduleTest {
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