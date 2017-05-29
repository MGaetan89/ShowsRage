package com.mgaetan89.showsrage.extension.realm

import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getEpisode
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetEpisodeTest : RealmTest() {
	@Before
	fun before() {
		this.realm.isAutoRefresh = true
	}

	@Test
	@UiThreadTest
	fun getEpisode() {
		this.realm.getEpisode("72173_4_1", RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it.id).isEqualTo("72173_4_1")
			assertThat(it.name).isEqualTo("Flight of the Phoenix")
		})
	}

	@Test
	@UiThreadTest
	fun getEpisode_notFound() {
		this.realm.getEpisode("0_0_0", RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it.id).isEqualTo("0_0_0")
			assertThat(it.name).isEqualTo("")
		})
	}
}
