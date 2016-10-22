package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.saveShow
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.Quality
import com.mgaetan89.showsrage.model.RealmString
import com.mgaetan89.showsrage.model.Show
import io.realm.Realm
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

        this.realm.isAutoRefresh = false

        assertThat(this.getShows()).hasSize(83)
    }

    @Test
    fun saveShow() {
        val show = Show().apply {
            this.airs = "Monday 9:00 PM"
            this.genre = RealmList(buildRealmString("Action"), buildRealmString("Drama"))
            this.imdbId = "tt123456"
            this.indexerId = 42
            this.location = "/home/videos/Show Name"
            this.qualityDetails = null
            this.seasonList = RealmList(buildRealmString("2"), buildRealmString("1"))
        }

        this.realm.saveShow(show)

        this.validateShow(show.airs, show.genre, show.imdbId, show.indexerId, show.location, show.qualityDetails, show.seasonList)
    }

    @Test
    fun saveShow_update() {
        val show = Show().apply {
            this.airs = "Thursday 10:00 PM"
            this.genre = RealmList(buildRealmString("Action"), buildRealmString("Comedy"))
            this.imdbId = "tt1234567"
            this.indexerId = 42
            this.location = "/home/videos/Show Name"
            this.qualityDetails = Quality().apply {
                this.archive = RealmList(buildRealmString("fullhdwebdl"), buildRealmString("fullhdbluray"))
                this.indexerId = 42
                this.initial = RealmList(buildRealmString("fullhdtv"))
            }
            this.seasonList = RealmList(buildRealmString("3"), buildRealmString("2"), buildRealmString("1"))
        }

        this.realm.saveShow(show)

        this.validateShow(show.airs, show.genre, show.imdbId, show.indexerId, show.location, show.qualityDetails, show.seasonList)
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun buildRealmString(value: String) = RealmString().apply { this.value = value }

    private fun getShow(indexerId: Int) = this.realm.getShow(indexerId)

    private fun getShows() = this.realm.getShows(null)

    private fun validateShow(airs: String?, genre: RealmList<RealmString>?, imdbId: String?, indexerId: Int, location: String?, qualityDetails: Quality?, seasonList: RealmList<RealmString>?) {
        assertThat(this.getShows()).hasSize(84)

        val show = this.getShow(indexerId)

        assertThat(show).isNotNull()
        assertThat(show!!.airs).isEqualTo(airs)
        assertThat(show.genre).containsExactly(*genre!!.toTypedArray())
        assertThat(show.imdbId).isEqualTo(imdbId)
        assertThat(show.indexerId).isEqualTo(indexerId)
        assertThat(show.location).isEqualTo(location)

        if (qualityDetails == null) {
            assertThat(show.qualityDetails).isNull()
        } else {
            show.qualityDetails!!.let {
                assertThat(it.archive).containsExactly(*qualityDetails.archive!!.toTypedArray())
                assertThat(it.indexerId).isEqualTo(indexerId)
                assertThat(it.initial).containsExactly(*qualityDetails.initial!!.toTypedArray())
            }
        }

        assertThat(show.seasonList).containsExactly(*seasonList!!.toTypedArray())
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
