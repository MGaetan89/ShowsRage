package com.mgaetan89.showsrage.extension

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.initRealm
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
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())

        assertThat(this.getHistory()).hasSize(93)
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
