package com.mgaetan89.showsrage.widget

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ServiceTestRule
import android.support.test.runner.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScheduleWidgetServiceTest {
    @JvmField
    @Rule
    val serviceRule = ServiceTestRule()

    private lateinit var service: ScheduleWidgetService

    @Before
    fun before() {
        val intent = Intent(InstrumentationRegistry.getTargetContext(), ScheduleWidgetService::class.java)
        val bindService = this.serviceRule.bindService(intent)

        this.service = (bindService as ScheduleWidgetService.ServiceBinder).service
    }

    @Test
    fun onGetViewFactory() {
        assertThat(this.service.onGetViewFactory(null)).isExactlyInstanceOf(ScheduleWidgetFactory::class.java)
    }
}
