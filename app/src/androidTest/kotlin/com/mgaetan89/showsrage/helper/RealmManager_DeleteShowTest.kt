package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.Show
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_DeleteShowTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var show: Show

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())

        this.show = RealmManager.getShow(INDEXER_ID, null)!!

        this.validateShow(this.show)

        // TODO Handle other delete make by this method
    }

    @Ignore
    @Test
    fun deleteShow() {
        RealmManager.deleteShow(INDEXER_ID)

        assertThat(RealmManager.getShows(null, null)?.size).isEqualTo(76)

        val show = RealmManager.getShow(INDEXER_ID, null)

        assertThat(show).isNull()
    }

    @After
    fun after() {
        RealmManager.saveShow(this.show)

        assertThat(RealmManager.getShows(null, null)?.size).isEqualTo(77)

        RealmManager.close()
    }

    private fun validateShow(show: Show) {
        assertThat(show).isNotNull()
        assertThat(show.indexerId).isEqualTo(INDEXER_ID)
        assertThat(RealmManager.getShows(null, null)?.size).isEqualTo(77)
    }

    companion object {
        private const val INDEXER_ID = 75760

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
