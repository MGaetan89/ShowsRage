package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveEpisode
import com.mgaetan89.showsrage.model.OmDbEpisode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveOmdbEpisodeTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getEpisodes()).hasSize(90)
    }

    @Test
    fun saveEpisode() {
        val episode = OmDbEpisode().apply {
            this.episode = EPISODE_NUMBER
            this.season = SEASON_NUMBER
            this.seriesId = SERIES_ID
            this.title = "Episode name"
        }

        this.realm.saveEpisode(episode)

        this.validateEpisode("Episode name")
    }

    @Test
    fun saveEpisode_update() {
        // First we save an episode
        this.saveEpisode()

        // Then we update it
        val episode = OmDbEpisode().apply {
            this.episode = EPISODE_NUMBER
            this.season = SEASON_NUMBER
            this.seriesId = SERIES_ID
        }

        this.realm.saveEpisode(episode)

        this.validateEpisode(null)
    }

    private fun getEpisode(id: String) = this.realm.where(OmDbEpisode::class.java).equalTo("id", id).findFirst()

    private fun getEpisodes() = this.realm.where(OmDbEpisode::class.java).findAll()

    private fun validateEpisode(episodeName: String?) {
        assertThat(this.getEpisodes()).hasSize(91)

        val episode = this.getEpisode(EPISODE_ID)

        assertThat(episode).isNotNull()
        assertThat(episode!!.episode).isEqualTo(EPISODE_NUMBER)
        assertThat(episode.id).isEqualTo(EPISODE_ID)
        assertThat(episode.season).isEqualTo(SEASON_NUMBER)
        assertThat(episode.seriesId).isEqualTo(SERIES_ID)
        assertThat(episode.title).isEqualTo(episodeName)
    }

    companion object {
        private const val EPISODE_NUMBER = "1"
        private const val SEASON_NUMBER = "5"
        private const val SERIES_ID = "tt2193021"
        private val EPISODE_ID = OmDbEpisode.buildId(SERIES_ID, SEASON_NUMBER, EPISODE_NUMBER)
    }
}
