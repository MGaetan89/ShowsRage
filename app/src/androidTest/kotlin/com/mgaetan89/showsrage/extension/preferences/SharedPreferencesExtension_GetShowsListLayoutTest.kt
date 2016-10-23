package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getShowsListLayout
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetShowsListLayoutTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun getShowsListLayout() {
        this.preference.edit().putString(Fields.SHOWS_LIST_LAYOUT.field, "showsListLayout").apply()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_poster)
    }

    @Test
    fun getShowsListLayout_Banner() {
        this.preference.edit().putString(Fields.SHOWS_LIST_LAYOUT.field, "banner").apply()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_banner)
    }

    @Test
    fun getShowsListLayout_Empty() {
        this.preference.edit().putString(Fields.SHOWS_LIST_LAYOUT.field, "").apply()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_poster)
    }

    @Test
    fun getShowsListLayout_FanArt() {
        this.preference.edit().putString(Fields.SHOWS_LIST_LAYOUT.field, "fan_art").apply()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_poster)
    }

    @Test
    fun getShowsListLayout_Missing() {
        assertThat(this.preference.contains(Fields.SHOWS_LIST_LAYOUT.field)).isFalse()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_poster)
    }

    @Test
    fun getShowsListLayout_Null() {
        this.preference.edit().putString(Fields.SHOWS_LIST_LAYOUT.field, null).apply()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_poster)
    }

    @Test
    fun getShowsListLayout_poster() {
        this.preference.edit().putString(Fields.SHOWS_LIST_LAYOUT.field, "poster").apply()

        val showsListLayout = this.preference.getShowsListLayout()

        assertThat(showsListLayout).isEqualTo(R.layout.adapter_shows_list_content_poster)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
