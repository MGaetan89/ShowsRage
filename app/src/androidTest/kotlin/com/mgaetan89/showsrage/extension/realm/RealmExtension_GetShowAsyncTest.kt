package com.mgaetan89.showsrage.extension.realm

import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShow
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowAsyncTest : RealmTest() {
	@Before
	fun before() {
		this.realm.isAutoRefresh = true
	}

	@Test
	@UiThreadTest
	fun getShow() {
		this.realm.getShow(292174, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it.indexerId).isEqualTo(292174292174)
			assertThat(it.showName).isEqualTo("Dark Matter")
		})
	}

	@Test
	@UiThreadTest
	fun getShow_notFound() {
		this.realm.getShow(0, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it.indexerId).isEqualTo(0)
			assertThat(it.showName).isEqualTo("")
		})
	}
}
