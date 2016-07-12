package com.mgaetan89.showsrage.presenter

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class HistoryPresenterTest(
        val history: History?, val episode: Int, val posterUrl: String, val provider: String,
        val providerQuality: String?, val quality: String, val season: Int, val showName: String
) {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var presenter: HistoryPresenter

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())

        val realmHistory = if (this.history != null) {
            RealmManager.getRealm()?.copyToRealm(this.history)
        } else {
            this.history
        }

        this.presenter = HistoryPresenter(realmHistory)
    }

    @Test
    fun getEpisode() {
        assertThat(this.presenter.getEpisode()).isEqualTo(this.episode)
    }

    @Test
    fun getPosterUrl() {
        assertThat(this.presenter.getPosterUrl()).isEqualTo(this.posterUrl)
    }

    @Test
    fun getProvider() {
        assertThat(this.presenter.getProvider()).isEqualTo(this.provider)
    }

    @Test
    fun getProviderQuality() {
        assertThat(this.presenter.getProviderQuality()).isEqualTo(this.providerQuality)
    }

    @Test
    fun getQuality() {
        assertThat(this.presenter.getQuality()).isEqualTo(this.quality)
    }

    @Test
    fun getSeason() {
        assertThat(this.presenter.getSeason()).isEqualTo(this.season)
    }

    @Test
    fun getShowName() {
        assertThat(this.presenter.getShowName()).isEqualTo(this.showName)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
        }

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            val gson = SickRageApi.gson

            return listOf(
                    arrayOf(null as History?, 0, "", "", null, "", 0, ""),
                    arrayOf(gson.fromJson("{episode: 1, provider: \"CtrlHD\", quality: \"HD1080p\", season: 2, show_name: \"Show 1\", tvdbid: 123}", History::class.java), 1, "?cmd=show.getposter&tvdbid=123", "CtrlHD", null, "HD1080p", 2, "Show 1"),
                    arrayOf(gson.fromJson("{episode: 2, provider: \"-1\", quality: \"HD\", season: 3, show_name: \"Show 2\", tvdbid: 456}", History::class.java), 2, "?cmd=show.getposter&tvdbid=456", "-1", "HD", "HD", 3, "Show 2")
            )
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Looper.myLooper().quit()
        }
    }
}
