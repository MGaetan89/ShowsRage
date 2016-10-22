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
            this.genre = RealmList(RealmString("Action"), RealmString("Drama"))
            this.imdbId = "tt123456"
            this.indexerId = 42
            this.location = "/home/videos/Show Name"
            this.qualityDetails = null
            this.seasonList = RealmList(RealmString("2"), RealmString("1"))
        }

        this.realm.saveShow(show)

        this.validateShow(show.airs, show.genre, show.imdbId, show.indexerId, show.location, show.qualityDetails, show.seasonList)
    }

    @Test
    fun saveShow_update() {
        val show = Show().apply {
            this.airs = "Thursday 10:00 PM"
            this.genre = RealmList(RealmString("Action"), RealmString("Comedy"))
            this.imdbId = "tt1234567"
            this.indexerId = 42
            this.location = "/home/videos/Show Name"
            this.qualityDetails = Quality().apply {
                this.archive = RealmList(RealmString("fullhdwebdl"), RealmString("fullhdbluray"))
                this.indexerId = 42
                this.initial = RealmList(RealmString("fullhdtv"))
            }
            this.seasonList = RealmList(RealmString("3"), RealmString("2"), RealmString("1"))
        }

        this.realm.saveShow(show)

        this.validateShow(show.airs, show.genre, show.imdbId, show.indexerId, show.location, show.qualityDetails, show.seasonList)
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getShow(indexerId: Int) = this.realm.getShow(indexerId)

    private fun getShows() = this.realm.getShows(null)

    private fun validateRealmList(actual: RealmList<RealmString>?, expected: RealmList<RealmString>?) {
        if (expected == null) {
            assertThat(actual).isNull()
        } else {
            assertThat(actual).isNotNull()
            assertThat(actual).hasSize(expected.size)

            actual!!.forEachIndexed { i, item ->
                assertThat(item.value).isEqualTo(expected[i].value)
            }
        }
    }

    private fun validateShow(airs: String?, genre: RealmList<RealmString>?, imdbId: String?, indexerId: Int, location: String?, qualityDetails: Quality?, seasonList: RealmList<RealmString>?) {
        assertThat(this.getShows()).hasSize(84)

        val show = this.getShow(indexerId)

        assertThat(show).isNotNull()
        assertThat(show!!.airs).isEqualTo(airs)

        this.validateRealmList(show.genre, genre)

        assertThat(show.imdbId).isEqualTo(imdbId)
        assertThat(show.indexerId).isEqualTo(indexerId)
        assertThat(show.location).isEqualTo(location)

        if (qualityDetails == null) {
            assertThat(show.qualityDetails).isNull()
        } else {
            assertThat(show.qualityDetails).isNotNull()

            show.qualityDetails!!.let {
                this.validateRealmList(it.archive, qualityDetails.archive)

                assertThat(it.indexerId).isEqualTo(indexerId)

                this.validateRealmList(it.initial, qualityDetails.initial)
            }
        }

        this.validateRealmList(show.seasonList, seasonList)
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
