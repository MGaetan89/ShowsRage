package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getLogsGroup
import com.mgaetan89.showsrage.initRealm
import io.realm.Realm
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetLogsGroupTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getLogsGroup() {
        val logsGroup = this.realm.getLogsGroup()

        assertThat(logsGroup).hasSize(4)
        assertThat(logsGroup).containsExactly("POSTPROCESSER", "SEARCHQUEUE-BACKLOG-75897", "SEARCHQUEUE-DAILY-SEARCH", "TORNADO")
    }

    @After
    fun after() {
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
