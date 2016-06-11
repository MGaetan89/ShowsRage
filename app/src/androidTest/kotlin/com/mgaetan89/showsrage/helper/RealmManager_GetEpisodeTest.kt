package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.Episode
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_GetEpisodeTest {
    private var episodeExistsAsync: Episode? = null
    private val episodeExistsAsyncListener = RealmChangeListener<Episode> {
        this.episodeExistsAsync?.removeChangeListeners()

        this.validateEpisodeExists(it)
    }

    private var episodeNotExistsAsync: Episode? = null
    private val episodeNotExistsAsyncListener = RealmChangeListener<Episode> {
        this.episodeNotExistsAsync?.removeChangeListeners()

        this.validateEpisodeNotExists(it)
    }

    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getEpisodeExistsAsync() {
        this.episodeExistsAsync = RealmManager.getEpisode("121361_6_6", this.episodeExistsAsyncListener)
    }

    @Test
    fun getEpisodeNotExistsAsync() {
        this.episodeNotExistsAsync = RealmManager.getEpisode("121361_0_0", this.episodeNotExistsAsyncListener)
    }

    @Test
    fun getEpisodeExistsSync() {
        val episode = RealmManager.getEpisode("121361_6_6", null)

        this.validateEpisodeExists(episode)
    }

    @Test
    fun getEpisodeNotExistsSync() {
        val episode = RealmManager.getEpisode("121361_0_0", null)

        this.validateEpisodeNotExists(episode)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    private fun validateEpisodeExists(episode: Episode?) {
        assertThat(episode).isNotNull()
        assertThat(episode!!.airDate).isEqualTo("2016-05-29")
        assertThat(episode.description).startsWith("An old foe comes back")
        assertThat(episode.fileSize).isEqualTo(3490344455)
        assertThat(episode.fileSizeHuman).isEqualTo("3.25 GB")
        assertThat(episode.id).isEqualTo("121361_6_6")
        assertThat(episode.indexerId).isEqualTo(121361)
        assertThat(episode.location).isEqualTo("/volume1/Séries/Game of Thrones/Saison 6/6x06 - Blood of My Blood [1080p HDTV].mkv")
        assertThat(episode.name).isEqualTo("Blood of My Blood")
        assertThat(episode.number).isEqualTo(6)
        assertThat(episode.quality).isEqualTo("1080p HDTV")
        assertThat(episode.releaseName).isEqualTo("Game.of.Thrones.S06E06.1080p.HDTV.x264-BATV")
        assertThat(episode.season).isEqualTo(6)
        assertThat(episode.status).isEqualTo("Downloaded")
        assertThat(episode.subtitles).isEqualTo("eng")
    }

    private fun validateEpisodeNotExists(episode: Episode?) {
        assertThat(episode).isNull()
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
