package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.History
import io.realm.RealmChangeListener
import io.realm.RealmResults
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_GetHistoryTest {
    private var historyAsync: RealmResults<History>? = null
    private val historyAsyncListener = RealmChangeListener<RealmResults<History>> {
        this.historyAsync?.removeChangeListeners()

        this.validateHistory(it)
    }

    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getHistoryAsync() {
        this.historyAsync = RealmManager.getHistory(this.historyAsyncListener)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    private fun validateHistory(history: RealmResults<History>) {
        assertThat(history).isNotNull()
        assertThat(history).hasSize(93)

        for (i in 1..history.size - 1) {
            val previous = history[i - 1].date!!
            val current = history[i].date!!

            if (previous < current) {
                fail("History is not properly sorted: %s < %s!".format(previous, current))
            }
        }
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
