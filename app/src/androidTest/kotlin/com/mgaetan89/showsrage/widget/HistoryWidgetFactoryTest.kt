package com.mgaetan89.showsrage.widget

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.presenter.HistoryPresenter
import com.mgaetan89.showsrage.presenter.SchedulePresenter
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class HistoryWidgetFactoryTest : ListWidgetFactoryTest<HistoryWidgetFactory>() {
    @Before
    fun before() {
        this.factory = HistoryWidgetFactory(this.activityRule.activity)
    }

    @Test
    fun getEpisodeTitle() {
        val presenter = mock(HistoryPresenter::class.java)
        `when`(presenter.getEpisode()).thenReturn(2)
        `when`(presenter.getSeason()).thenReturn(4)
        `when`(presenter.getShowName()).thenReturn("Show Name")

        assertThat(this.factory.getEpisodeTitle(presenter)).isEqualTo("Show Name - S04E02")
    }
}
