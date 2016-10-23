package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.saveShowsStat
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.ShowsStat
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
class RealmExtension_SaveShowsStatTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

        this.realm.isAutoRefresh = false

        assertThat(this.getShowsStats()).hasSize(1)
    }

    @Test
    fun saveShowsStat() {
        this.realm.saveShowsStat(ShowsStat().apply {
            this.episodesDownloaded = 5
            this.episodesSnatched = 10
            this.episodesTotal = 50
            this.showsActive = 20
            this.showsTotal = 25
        })

        val showsStats = this.getShowsStats()
        assertThat(showsStats).hasSize(1)

        val showsStat = showsStats.first()
        assertThat(showsStat).isNotNull()
        assertThat(showsStat.episodesDownloaded).isEqualTo(5)
        assertThat(showsStat.episodesMissing).isEqualTo(35)
        assertThat(showsStat.episodesSnatched).isEqualTo(10)
        assertThat(showsStat.episodesTotal).isEqualTo(50)
        assertThat(showsStat.showsActive).isEqualTo(20)
        assertThat(showsStat.showsTotal).isEqualTo(25)
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getShowsStats() = this.realm.where(ShowsStat::class.java).findAll()

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
