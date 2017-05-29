package com.mgaetan89.showsrage.widget

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.presenter.SchedulePresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScheduleWidgetFactoryTest : ListWidgetFactoryTest<ScheduleWidgetFactory>() {
	@Before
	fun before() {
		this.factory = ScheduleWidgetFactory(this.activityRule.activity)
	}

	@Test
	fun getEpisodeTitle() {
		val schedule = Schedule().apply {
			this.episode = 2
			this.season = 4
			this.showName = "Show Name"
		}
		val presenter = SchedulePresenter(schedule, null)

		assertThat(this.factory.getEpisodeTitle(presenter)).isEqualTo("Show Name - S04E02")
	}
}
