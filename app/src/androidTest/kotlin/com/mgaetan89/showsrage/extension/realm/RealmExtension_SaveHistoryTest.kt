package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.saveHistory
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
class RealmExtension_SaveHistoryTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

        this.realm.isAutoRefresh = false

        assertThat(this.getHistory()).hasSize(100)
    }

    @Test
    fun saveHistory() {
        val historiesToSave = mutableListOf<History>()

        for (i in 1..3) {
            historiesToSave.add(History().apply {
                this.episode = i
                this.date = "date_$i"
                this.indexerId = INDEXER_ID
                this.season = i * 10
                this.status = "status_$i"
            })
        }

        this.realm.saveHistory(historiesToSave)

        val histories = this.getHistory()

        assertThat(histories).hasSize(3)

        for (i in 1..3) {
            val history = histories[i - 1]

            assertThat(history.episode).isEqualTo(i)
            assertThat(history.date).isEqualTo("date_$i")
            assertThat(history.id).isEqualTo("date_${i}_status_${i}_${INDEXER_ID}_${i * 10}_$i")
            assertThat(history.indexerId).isEqualTo(INDEXER_ID)
            assertThat(history.season).isEqualTo(i * 10)
            assertThat(history.status).isEqualTo("status_$i")
        }
    }

    @Test
    fun saveHistory_empty() {
        this.realm.saveHistory(emptyList())

        assertThat(this.getHistory()).hasSize(0)
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getHistory() = this.realm.where(History::class.java).findAll()

    companion object {
        private const val INDEXER_ID = 73838

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
