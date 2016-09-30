package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useDarkTheme
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_UseDarkThemeTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun useDarkTheme_False() {
        this.preference.edit().putBoolean(Fields.THEME.field, false).apply()

        val useDarkTheme = this.preference.useDarkTheme()

        assertThat(useDarkTheme).isFalse()
    }

    @Test
    fun useDarkTheme_Missing() {
        this.preference.edit().putBoolean(Fields.THEME.field, false).apply()

        val useDarkTheme = this.preference.useDarkTheme()

        assertThat(useDarkTheme).isFalse()
    }

    @Test
    fun useDarkTheme_Null() {
        val useDarkTheme = null.useDarkTheme()

        assertThat(useDarkTheme).isTrue()
    }

    @Test
    fun useDarkTheme_True() {
        this.preference.edit().putBoolean(Fields.THEME.field, true).apply()

        val useDarkTheme = this.preference.useDarkTheme()

        assertThat(useDarkTheme).isTrue()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
