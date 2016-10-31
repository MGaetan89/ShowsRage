package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.saveEpisode
import com.mgaetan89.showsrage.model.Episode
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
class RealmExtension_SaveEpisodeTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        this.realm.isAutoRefresh = false

        assertThat(this.getEpisodes()).hasSize(1647)
    }

    @Test
    fun saveEpisode() {
        val episode = Episode().apply {
            this.name = "Episode name"
        }

        this.realm.saveEpisode(episode, INDEXER_ID, SEASON_NUMBER, EPISODE_NUMBER)

        this.validateEpisode("Episode name", null, null)
    }

    @Test
    fun saveEpisode_update() {
        // First we save an episode
        this.saveEpisode()

        // Then we update it
        val episode = Episode().apply {
            this.description = "Episode description"
            this.fileSizeHuman = "3.5 GB"
        }

        this.realm.saveEpisode(episode, INDEXER_ID, SEASON_NUMBER, EPISODE_NUMBER)

        this.validateEpisode("", "Episode description", "3.5 GB")
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getEpisode(id: String) = this.realm.where(Episode::class.java).equalTo("id", id).findFirst()

    private fun getEpisodes() = this.realm.where(Episode::class.java).findAll()

    private fun validateEpisode(episodeName: String?, description: String?, fileSizeHuman: String?) {
        assertThat(this.getEpisodes()).hasSize(1648)

        val episode = this.getEpisode(EPISODE_ID)

        assertThat(episode).isNotNull()
        assertThat(episode!!.description).isEqualTo(description)
        assertThat(episode.fileSizeHuman).isEqualTo(fileSizeHuman)
        assertThat(episode.id).isEqualTo(EPISODE_ID)
        assertThat(episode.indexerId).isEqualTo(INDEXER_ID)
        assertThat(episode.name).isEqualTo(episodeName)
        assertThat(episode.number).isEqualTo(EPISODE_NUMBER)
        assertThat(episode.season).isEqualTo(SEASON_NUMBER)
    }

    companion object {
        private const val EPISODE_NUMBER = 1
        private const val SEASON_NUMBER = 8
        private const val INDEXER_ID = 73838
        private val EPISODE_ID = Episode.buildId(INDEXER_ID, SEASON_NUMBER, EPISODE_NUMBER)

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
