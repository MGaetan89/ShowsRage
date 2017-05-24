package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveEpisodes
import com.mgaetan89.showsrage.model.Episode
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveEpisodesTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getEpisodes()).hasSize(1647)
    }

    @Test
    fun saveEpisodes() {
        val episodes = (1..5).map {
            Episode().apply {
                this.name = "Episode $it"
                this.number = it
            }
        }

        this.realm.saveEpisodes(episodes, INDEXER_ID, SEASON_NUMBER)

        assertThat(this.getEpisodes()).hasSize(1652)

        for (i in 1..5) {
            this.validateEpisode(i, "Episode $i", null, null)
        }
    }

    @Test
    fun saveEpisodes_empty() {
        this.realm.saveEpisodes(emptyList(), INDEXER_ID, SEASON_NUMBER)

        assertThat(this.getEpisodes()).hasSize(1647)
    }

    @Test
    fun saveEpisodes_update() {
        // First we save some episodes
        this.saveEpisodes()

        // Then we perform some updates
        val episodes = (1..5).map {
            Episode().apply {
                this.description = "Episode $it description"
                this.fileSizeHuman = "$it GB"
                this.number = it
            }
        }

        this.realm.saveEpisodes(episodes, INDEXER_ID, SEASON_NUMBER)

        assertThat(this.getEpisodes()).hasSize(1652)

        for (i in 1..5) {
            this.validateEpisode(i, "", "Episode $i description", "$i GB")
        }
    }

    private fun getEpisode(id: String) = this.realm.where(Episode::class.java).equalTo("id", id).findFirst()

    private fun getEpisodes() = this.realm.where(Episode::class.java).findAll()

    private fun validateEpisode(episodeNumber: Int, episodeName: String?, description: String?, fileSizeHuman: String?) {
        val episodeId = Episode.buildId(INDEXER_ID, SEASON_NUMBER, episodeNumber)
        val episode = this.getEpisode(episodeId)

        assertThat(episode).isNotNull()
        assertThat(episode!!.description).isEqualTo(description)
        assertThat(episode.fileSizeHuman).isEqualTo(fileSizeHuman)
        assertThat(episode.id).isEqualTo(episodeId)
        assertThat(episode.indexerId).isEqualTo(INDEXER_ID)
        assertThat(episode.name).isEqualTo(episodeName)
        assertThat(episode.number).isEqualTo(episodeNumber)
        assertThat(episode.season).isEqualTo(SEASON_NUMBER)
    }

    companion object {
        private const val SEASON_NUMBER = 8
        private const val INDEXER_ID = 73838
    }
}
