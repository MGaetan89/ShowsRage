package com.mgaetan89.showsrage.widget

import android.appwidget.AppWidgetManager
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScheduleWidgetProviderTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var provider: ScheduleWidgetProvider

    @Before
    fun before() {
        this.provider = ScheduleWidgetProvider()
    }

    @Test
    fun getListAdapterIntent() {
        val intent = this.provider.getListAdapterIntent(this.activityRule.activity, 42)

        assertThat(intent).isNotNull()
        assertThat(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)).isEqualTo(42)
        assertThat(intent.component.className).isEqualTo(ScheduleWidgetService::class.java.name)
    }

    @Test
    fun getListAdapterIntentNoContext() {
        val intent = this.provider.getListAdapterIntent(null, 42)

        assertThat(intent).isNotNull()
        assertThat(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)).isEqualTo(42)
        assertThat(intent.component.className).isEqualTo(ScheduleWidgetService::class.java.name)
    }

    @Test
    fun getWidgetEmptyText() {
        assertThat(this.provider.getWidgetEmptyText(this.activityRule.activity)).isEqualTo("No coming episodes")
    }

    @Test
    fun getWidgetEmptyTextNoContext() {
        assertThat(this.provider.getWidgetEmptyText(null)).isNull()
    }

    @Test
    fun getWidgetTitle() {
        assertThat(this.provider.getWidgetTitle(this.activityRule.activity)).isEqualTo("Schedule")
    }

    @Test
    fun getWidgetTitleNoContext() {
        assertThat(this.provider.getWidgetTitle(null)).isNull()
    }
}
