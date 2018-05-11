package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.buildComparator
import com.mgaetan89.showsrage.extension.getHistory
import com.mgaetan89.showsrage.model.History
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetHistoryTest : RealmTest() {
	@Before
	fun before() {
		this.realm.isAutoRefresh = true
	}

	@Test
	fun getHistory() {
		this.realm.getHistory(RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).hasSize(100)
			assertThat(it).isSortedAccordingTo(buildComparator(History::date, true))
		})
	}
}
