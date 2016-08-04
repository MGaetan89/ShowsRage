package com.mgaetan89.showsrage.extension

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import com.mgaetan89.showsrage.TestActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

@RunWith(Parameterized::class)
class SharedPreferencesExtension_GetLocaleTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getLocale_Missing_SupportedLocale() {
        Locale.setDefault(Locale.FRENCH)

        assertThat(this.preference.contains(Fields.DISPLAY_LANGUAGE.field)).isFalse()

        val locale = this.preference.getLocale()

        assertThat(locale).isEqualTo(Locale.FRENCH)
    }

    @Test
    fun getLocale_Missing_UnsupportedLocale() {
        Locale.setDefault(Locale.GERMAN)

        assertThat(this.preference.contains(Fields.DISPLAY_LANGUAGE.field)).isFalse()

        val locale = this.preference.getLocale()

        assertThat(locale).isEqualTo(Locale.ENGLISH)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
