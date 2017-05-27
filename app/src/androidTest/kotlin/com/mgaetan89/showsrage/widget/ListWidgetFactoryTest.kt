package com.mgaetan89.showsrage.widget

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class ListWidgetFactoryTest<T : ListWidgetFactory<Any>> {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule(TestActivity::class.java, false, true)

	protected lateinit var factory: T

	@Test
	fun getItemId() {
		assertThat(this.factory.getItemId(-1)).isEqualTo(-1L)
		assertThat(this.factory.getItemId(0)).isEqualTo(0L)
		assertThat(this.factory.getItemId(1)).isEqualTo(1L)
	}

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
		this.activityRule.activity.getPreferences().edit().putBoolean(Fields.THEME.field, true).apply()

		this.factory.setLayoutFiles()

		assertThat(this.factory.itemLayout).isEqualTo(R.layout.widget_list_adapter_dark)
		assertThat(this.factory.loadingLayout).isEqualTo(R.layout.widget_adapter_loading_dark)
	}

	@Test
	fun setLayoutFiles_lightTheme() {
		this.activityRule.activity.getPreferences().edit().putBoolean(Fields.THEME.field, false).apply()

		this.factory.setLayoutFiles()

		assertThat(this.factory.itemLayout).isEqualTo(R.layout.widget_list_adapter_light)
		assertThat(this.factory.loadingLayout).isEqualTo(R.layout.widget_adapter_loading_light)
	}
}
