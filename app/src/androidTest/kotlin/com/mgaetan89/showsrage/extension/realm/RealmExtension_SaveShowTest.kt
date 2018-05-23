package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.saveShow
import com.mgaetan89.showsrage.model.Quality
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.validateRealmList
import io.realm.RealmList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowTest : RealmTest() {
	@Before
	fun before() {
		assertThat(this.getShows()).hasSize(83)
	}

	@Test
	fun saveShow() {
		val show = Show().apply {
			this.airs = "Monday 9:00 PM"
			this.genre = RealmList("Action", "Drama")
			this.imdbId = "tt123456"
			this.indexerId = 42
			this.location = "/home/videos/Show Name"
			this.qualityDetails = null
			this.seasonList = RealmList("2", "1")
		}

		this.realm.saveShow(show)

		this.validateShow(show.airs, show.genre, show.imdbId, show.indexerId, show.location, show.qualityDetails, show.seasonList)
	}

	@Test
	fun saveShow_update() {
		val show = Show().apply {
			this.airs = "Thursday 10:00 PM"
			this.genre = RealmList("Action", "Comedy")
			this.imdbId = "tt1234567"
			this.indexerId = 42
			this.location = "/home/videos/Show Name"
			this.qualityDetails = Quality().apply {
				this.archive = RealmList("fullhdwebdl", "fullhdbluray")
				this.indexerId = 42
				this.initial = RealmList("fullhdtv")
			}
			this.seasonList = RealmList("3", "2", "1")
		}

		this.realm.saveShow(show)

		this.validateShow(show.airs, show.genre, show.imdbId, show.indexerId, show.location, show.qualityDetails, show.seasonList)
	}

	private fun getShow(indexerId: Int) = this.realm.getShow(indexerId)

	private fun getShows() = this.realm.getShows(null)

	private fun validateShow(airs: String?, genre: RealmList<String>?, imdbId: String?, indexerId: Int, location: String?, qualityDetails: Quality?, seasonList: RealmList<String>?) {
		assertThat(this.getShows()).hasSize(84)

		val show = this.getShow(indexerId)

		assertThat(show).isNotNull()
		assertThat(show!!.airs).isEqualTo(airs)

		validateRealmList(show.genre, genre)

		assertThat(show.imdbId).isEqualTo(imdbId)
		assertThat(show.indexerId).isEqualTo(indexerId)
		assertThat(show.location).isEqualTo(location)

		if (qualityDetails == null) {
			assertThat(show.qualityDetails).isNull()
		} else {
			assertThat(show.qualityDetails).isNotNull()

			show.qualityDetails!!.let {
				validateRealmList(it.archive, qualityDetails.archive)

				assertThat(it.indexerId).isEqualTo(indexerId)

				validateRealmList(it.initial, qualityDetails.initial)
			}
		}

		validateRealmList(show.seasonList, seasonList)
	}
}
