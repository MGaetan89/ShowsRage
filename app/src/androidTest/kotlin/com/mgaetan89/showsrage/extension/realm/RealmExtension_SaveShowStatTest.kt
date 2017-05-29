package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveShowStat
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.ShowStat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowStatTest : RealmTest() {
	@Before
	fun before() {
		assertThat(this.getRealmShowStat()).hasSize(83)
	}

	@Test
	fun saveShowStat() {
		val realmShowStat = this.realm.saveShowStat(this.getShowStat(5, 45, 30), INDEXER_ID)

		this.validateRealmShowStat(realmShowStat, 15, 45, 65)
	}

	@Test
	fun saveShowStat_update() {
		// First we save some stat
		this.saveShowStat()

		// Then we update it
		val realmShowStat = this.realm.saveShowStat(this.getShowStat(60, 65, 70), INDEXER_ID)

		this.validateRealmShowStat(realmShowStat, 70, 65, 105)
	}

	private fun getRealmShowStat() = this.realm.where(RealmShowStat::class.java).findAll()

	private fun getShowStat(archived: Int, total: Int, snatched: Int): ShowStat {
		return ShowStat(
				archived = archived,
				downloaded = mapOf("total" to 10),
				failed = 15,
				ignored = 20,
				skipped = 25,
				snatched = mapOf("total" to snatched),
				snatchedBest = 35,
				subtitled = 40,
				total = total,
				unaired = 50,
				wanted = 55
		)
	}

	private fun validateRealmShowStat(realmShowStat: RealmShowStat, downloaded: Int, episodeCount: Int, snatched: Int) {
		assertThat(this.getRealmShowStat()).hasSize(84)
		assertThat(realmShowStat.downloaded).isEqualTo(downloaded)
		assertThat(realmShowStat.episodesCount).isEqualTo(episodeCount)
		assertThat(realmShowStat.indexerId).isEqualTo(INDEXER_ID)
		assertThat(realmShowStat.snatched).isEqualTo(snatched)
	}

	companion object {
		private const val INDEXER_ID = 73839
	}
}
