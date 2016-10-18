package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getEpisodes
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
class RealmExtension_GetOmdbEpisodesTest {
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
    fun getEpisodes() {
        this.realm.getEpisodes("tt2193021_4_18", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(1)
            assertThat(it.first().id).isEqualTo("tt2193021_4_18")
            assertThat(it.first().title).isEqualTo("Eleven-Fifty-Nine")
        })
    }

    @Test
    fun getEpisodes_notFound() {
        this.realm.getEpisodes("0_0_0", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(1)
            assertThat(it.first().id).isEqualTo("0_0_0")
            assertThat(it.first().title).isNull()
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
