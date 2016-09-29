package com.mgaetan89.showsrage.extension

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.initRealm
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
class RealmExtension_SaveEpisodesTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())

        assertThat(this.getEpisodes()).hasSize(1647)
    }

    @Test
    fun saveEpisodes() {
        val episodes = mutableListOf<Episode>()

        for (i in 1..5) {
            episodes.add(Episode().apply {
                this.name = "Episode $i"
                this.number = i
            })
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

        assertThat(this.getEpisodes()).hasSize(1648)
    }

    @Test
    fun saveEpisodes_update() {
        // First we save some episodes
        this.saveEpisodes()

        // Then we perform some updates
        val episodes = mutableListOf<Episode>()

        for (i in 1..5) {
            episodes.add(Episode().apply {
                this.description = "Episode $i description"
                this.fileSizeHuman = "$i GB"
                this.number = i
            })
        }

        this.realm.saveEpisodes(episodes, INDEXER_ID, SEASON_NUMBER)

        assertThat(this.getEpisodes()).hasSize(1652)

        for (i in 1..5) {
            this.validateEpisode(i, "", "Episode $i description", "$i GB")
        }
    }

    @After
    fun after() {
        this.realm.close()
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