package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShow
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
class RealmExtension_GetShowTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getShow() {
        val show = this.realm.getShow(INDEXER_ID)

        assertThat(show).isNotNull()
        assertThat(show!!.indexerId).isEqualTo(INDEXER_ID)
        assertThat(show.showName).isEqualTo("Futurama")
    }

    @Test
    fun getShow_unknown() {
        val show = this.realm.getShow(-1)

        assertThat(show).isNull()
    }

    @After
    fun after() {
        this.realm.close()
    }

    companion object {
        private const val INDEXER_ID = 73871

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
