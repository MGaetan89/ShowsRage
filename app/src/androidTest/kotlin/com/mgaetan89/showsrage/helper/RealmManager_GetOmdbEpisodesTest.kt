package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.OmDbEpisode
import io.realm.RealmChangeListener
import io.realm.RealmResults
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_GetOmdbEpisodesTest {
    private var episodesExistAsync: RealmResults<OmDbEpisode>? = null
    private val episodesExistAsyncListener = RealmChangeListener<RealmResults<OmDbEpisode>> {
        this.episodesExistAsync?.removeChangeListeners()

        this.validateEpisodesExist(it)
    }

    private var episodesNotExistAsync: RealmResults<OmDbEpisode>? = null
    private val episodesNotExistAsyncListener = RealmChangeListener<RealmResults<OmDbEpisode>> {
        this.episodesNotExistAsync?.removeChangeListeners()

        this.validateEpisodesNotExist(it)
    }

    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getEpisodesExistAsync() {
        this.episodesExistAsync = RealmManager.getEpisodes("tt0944947_6_7", this.episodesExistAsyncListener)
    }

    @Test
    fun getEpisodesNotExistAsync() {
        this.episodesNotExistAsync = RealmManager.getEpisodes("tt0944947_0_0", this.episodesNotExistAsyncListener)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    private fun validateEpisodesExist(episodes: RealmResults<OmDbEpisode>?) {
        assertThat(episodes).isNotNull()
        assertThat(episodes).hasSize(1)

        val episode = episodes!!.first()
        assertThat(episode).isNotNull()
        assertThat(episode!!.actors).isEqualTo("Peter Dinklage, Kit Harington, Liam Cunningham, Nikolaj Coster-Waldau")
        assertThat(episode.awards).isEqualTo("N/A")
        assertThat(episode.country).isEqualTo("USA")
        assertThat(episode.director).isEqualTo("Mark Mylod")
        assertThat(episode.episode).isEqualTo("7")
        assertThat(episode.genre).isEqualTo("Adventure, Drama, Fantasy")
        assertThat(episode.id).isEqualTo("tt0944947_6_7")
        assertThat(episode.imdbId).isEqualTo("tt4283060")
        assertThat(episode.imdbRating).isEqualTo("N/A")
        assertThat(episode.imdbVotes).isEqualTo("N/A")
        assertThat(episode.language).isEqualTo("English")
        assertThat(episode.metascore).isEqualTo("N/A")
        assertThat(episode.plot).startsWith("The High Sparrow")
        assertThat(episode.poster).isEqualTo("http://ia.media-imdb.com/images/M/MV5BMjM5OTQ1MTY5Nl5BMl5BanBnXkFtZTgwMjM3NzMxODE@._V1_SX300.jpg")
        assertThat(episode.rated).isEqualTo("TV-MA")
        assertThat(episode.released).isEqualTo("05 Jun 2016")
        assertThat(episode.response).isEqualTo("True")
        assertThat(episode.runtime).isEqualTo("56 min")
        assertThat(episode.season).isEqualTo("6")
        assertThat(episode.seriesId).isEqualTo("tt0944947")
        assertThat(episode.title).isEqualTo("The Broken Man")
        assertThat(episode.type).isEqualTo("episode")
        assertThat(episode.writer).isEqualTo("David Benioff (created by), George R.R. Martin (\"A Song of Ice and Fire\" by), D.B. Weiss (created by)")
        assertThat(episode.year).isEqualTo("2016")
    }

    private fun validateEpisodesNotExist(episodes: RealmResults<OmDbEpisode>?) {
        assertThat(episodes).isNotNull()
        assertThat(episodes).isEmpty()
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
