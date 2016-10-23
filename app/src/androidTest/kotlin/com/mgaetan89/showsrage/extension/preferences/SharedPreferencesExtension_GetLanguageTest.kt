package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getLanguage
import com.mgaetan89.showsrage.extension.getPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetLanguageTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun getLanguage() {
        this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "fr").apply()

        val language = this.preference.getLanguage()

        assertThat(language).isEqualTo("fr")
    }

    @Test
    fun getLanguage_Empty() {
        this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "").apply()

        val language = this.preference.getLanguage()

        assertThat(language).isEmpty()
    }

    @Test
    fun getLanguage_Missing() {
        assertThat(this.preference.contains(Fields.DISPLAY_LANGUAGE.field)).isFalse()

        val language = this.preference.getLanguage()

        assertThat(language).isEmpty()
    }

    @Test
    fun getLanguage_Null() {
        this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, null).apply()

        val language = this.preference.getLanguage()

        assertThat(language).isEmpty()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
