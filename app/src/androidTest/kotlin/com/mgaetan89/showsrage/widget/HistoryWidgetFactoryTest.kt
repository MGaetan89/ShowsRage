package com.mgaetan89.showsrage.widget

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.presenter.HistoryPresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryWidgetFactoryTest : ListWidgetFactoryTest<HistoryWidgetFactory>() {
	@Before
	fun before() {
		this.factory = HistoryWidgetFactory(this.activityRule.activity)
	}

	@Test
	fun getEpisodeTitle() {
		val history = History().apply {
			this.episode = 2
			this.season = 4
			this.showName = "Show Name"
		}
		val presenter = HistoryPresenter(history)

		assertThat(this.factory.getEpisodeTitle(presenter)).isEqualTo("Show Name - S04E02")
	}
}
