package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShowStat
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
class RealmExtension_GetShowStatTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getShowStat() {
        val showStat = this.realm.getShowStat(INDEXER_ID)

        assertThat(showStat).isNotNull()
        assertThat(showStat!!.downloaded).isEqualTo(82)
        assertThat(showStat.episodesCount).isEqualTo(120)
        assertThat(showStat.indexerId).isEqualTo(INDEXER_ID)
        assertThat(showStat.snatched).isEqualTo(21)
    }

    @Test
    fun getShowStat_unknown() {
        val showStat = this.realm.getShowStat(-1)

        assertThat(showStat).isNull()
    }

    @After
    fun after() {
        this.realm.close()
    }

    companion object {
        private const val INDEXER_ID = 248835

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
