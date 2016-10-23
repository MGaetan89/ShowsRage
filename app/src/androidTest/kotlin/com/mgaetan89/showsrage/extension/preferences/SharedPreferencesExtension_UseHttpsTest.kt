package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useHttps
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_UseHttpsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun useHttps_False() {
        this.preference.edit().putBoolean(Fields.HTTPS.field, false).apply()

        val useHttps = this.preference.useHttps()

        assertThat(useHttps).isFalse()
    }

    @Test
    fun useHttps_Missing() {
        this.preference.edit().putBoolean(Fields.HTTPS.field, false).apply()

        val useHttps = this.preference.useHttps()

        assertThat(useHttps).isFalse()
    }

    @Test
    fun useHttps_True() {
        this.preference.edit().putBoolean(Fields.HTTPS.field, true).apply()

        val useHttps = this.preference.useHttps()

        assertThat(useHttps).isTrue()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
