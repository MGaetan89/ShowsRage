package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShows
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowsAsyncTest : RealmTest() {
	@Before
	fun before() {
		this.realm.isAutoRefresh = true
	}

	@Test
	fun getShows_all() {
		this.realm.getShows(null, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).hasSize(83)
		})
	}

	@Test
	fun getShows_animes() {
		this.realm.getShows(true, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).hasSize(3)
		})
	}

	@Test
	fun getShows_show() {
		this.realm.getShows(false, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).hasSize(80)
		})
	}
}
