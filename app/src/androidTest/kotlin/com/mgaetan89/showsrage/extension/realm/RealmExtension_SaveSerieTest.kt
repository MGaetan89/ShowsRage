package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveSerie
import com.mgaetan89.showsrage.model.Serie
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveSerieTest : RealmTest() {
	@Before
	fun before() {
		assertThat(this.getSeries()).hasSize(25)
	}

	@Test
	fun saveSerie() {
		val serie = Serie().apply {
			this.genre = "Serie genre"
			this.imdbId = SERIE_ID
			this.title = "Serie title"
		}

		this.realm.saveSerie(serie)

		this.validateSerie("Serie title", null, "Serie genre")
	}

	@Test
	fun saveSerie_update() {
		// First we save a serie
		this.saveSerie()

		// Then we update it
		val serie = Serie().apply {
			this.genre = "Serie genre"
			this.imdbId = SERIE_ID
			this.plot = "Serie plot"
		}

		this.realm.saveSerie(serie)

		this.validateSerie(null, "Serie plot", "Serie genre")
	}

	private fun getSerie(serieId: String) = this.realm.where(Serie::class.java).equalTo("imdbId", serieId).findFirst()

	private fun getSeries() = this.realm.where(Serie::class.java).findAll()

	private fun validateSerie(title: String?, plot: String?, genre: String?) {
		assertThat(this.getSeries()).hasSize(26)

		val serie = this.getSerie(SERIE_ID)

		assertThat(serie).isNotNull()
		assertThat(serie.genre).isEqualTo(genre)
		assertThat(serie.plot).isEqualTo(plot)
		assertThat(serie.imdbId).isEqualTo(SERIE_ID)
		assertThat(serie.title).isEqualTo(title)
	}

	companion object {
		private const val SERIE_ID = "tt424242"
	}
}
