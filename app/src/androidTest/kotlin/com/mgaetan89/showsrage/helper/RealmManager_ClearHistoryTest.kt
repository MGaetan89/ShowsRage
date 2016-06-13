package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.History
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_ClearHistoryTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private var history: List<History>? = null

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())

        this.history = this.getHistory()

        this.validateNonEmptyHistory(this.history)
    }

    @Test
    fun clearHistory() {
        RealmManager.clearHistory()

        val history = this.getHistory()

        this.validateEmptyHistory(history)
    }

    @After
    fun after() {
        RealmManager.saveHistory(this.history ?: emptyList())
        RealmManager.close()
    }

    private fun getHistory(): List<History>? {
        return RealmManager.getRealm()?.let {
            it.copyFromRealm(it.where(History::class.java).findAll())
        }
    }

    private fun validateEmptyHistory(history: List<History>?) {
        assertThat(history).isNotNull()
        assertThat(history).isEmpty()
    }

    private fun validateNonEmptyHistory(history: List<History>?) {
        assertThat(history).isNotNull()
        assertThat(history).hasSize(93)
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
