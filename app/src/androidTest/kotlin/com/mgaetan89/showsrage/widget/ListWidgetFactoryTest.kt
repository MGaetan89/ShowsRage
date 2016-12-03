package com.mgaetan89.showsrage.widget

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useDarkTheme
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
abstract class ListWidgetFactoryTest<T : ListWidgetFactory<Any>> {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    protected lateinit var factory: T

    @Test
    fun getViewTypeCount() {
        assertThat(this.factory.viewTypeCount).isEqualTo(1)
    }

    @Test
    fun hasStableIds() {
        assertThat(this.factory.hasStableIds()).isTrue()
    }

    @Test
    fun setLayoutFiles_darkTheme() {
        `when`(this.activityRule.activity.getPreferences().useDarkTheme()).thenReturn(true)

        assertThat(this.factory.itemLayout).isEqualTo(R.layout.widget_list_adapter_dark)
        assertThat(this.factory.loadingLayout).isEqualTo(R.layout.widget_adapter_loading_dark)
    }

    @Test
    fun setLayoutFiles_lightTheme() {
        `when`(this.activityRule.activity.getPreferences().useDarkTheme()).thenReturn(true)

        assertThat(this.factory.itemLayout).isEqualTo(R.layout.widget_list_adapter_light)
        assertThat(this.factory.loadingLayout).isEqualTo(R.layout.widget_adapter_loading_light)
    }
}
