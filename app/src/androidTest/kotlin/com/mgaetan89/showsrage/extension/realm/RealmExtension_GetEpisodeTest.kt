package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getEpisode
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
class RealmExtension_GetEpisodeTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

        this.realm.isAutoRefresh = false
    }

    @Test
    fun getEpisode() {
        this.realm.getEpisode("72173_4_1", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it.id).isEqualTo("72173_4_1")
            assertThat(it.name).isEqualTo("Flight of the Phoenix")
        })
    }

    @Test
    fun getEpisode_notFound() {
        this.realm.getEpisode("0_0_0", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it.id).isEqualTo("0_0_0")
            assertThat(it.name).isEqualTo("")
        })
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
