package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.clearHistory
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.History
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
class RealmExtension_ClearHistoryTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        Utils.initRealm(InstrumentationRegistry.getContext(), "test.realm", deleteRealm = true)

        this.realm.isAutoRefresh = false

        assertThat(this.getHistory()).hasSize(100)
    }

    @Test
    fun clearHistory() {
        this.realm.clearHistory()

        assertThat(this.getHistory()).isEmpty()
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getHistory() = this.realm.where(History::class.java).findAll()

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
