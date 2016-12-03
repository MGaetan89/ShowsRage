package com.mgaetan89.showsrage.widget

import android.support.test.runner.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryWidgetServiceTest {
    @Test
    fun onGetViewFactory() {
        assertThat(HistoryWidgetService().onGetViewFactory(null)).isExactlyInstanceOf(HistoryWidgetFactory::class.java)
    }
}
