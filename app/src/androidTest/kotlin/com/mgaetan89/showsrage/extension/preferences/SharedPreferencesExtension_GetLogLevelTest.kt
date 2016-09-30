package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getLogLevel
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveLogLevel
import com.mgaetan89.showsrage.model.LogLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetLogLevelTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getLogLevel_Debug() {
        this.preference.saveLogLevel(LogLevel.DEBUG)

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.DEBUG)
    }

    @Test
    fun getLogLevel_Empty() {
        this.preference.edit().putString(Fields.LOGS_LEVEL.field, "").apply()

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.ERROR)
    }

    @Test
    fun getLogLevel_Error() {
        this.preference.saveLogLevel(LogLevel.ERROR)

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.ERROR)
    }

    @Test
    fun getLogLevel_Info() {
        this.preference.saveLogLevel(LogLevel.INFO)

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.INFO)
    }

    @Test
    fun getLogLevel_Missing() {
        assertThat(this.preference.contains(Fields.LOGS_LEVEL.field)).isFalse()

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.ERROR)
    }

    @Test
    fun getLogLevel_Null() {
        this.preference.edit().putString(Fields.LOGS_LEVEL.field, null).apply()

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.ERROR)
    }

    @Test
    fun getLogLevel_Warning() {
        this.preference.saveLogLevel(LogLevel.WARNING)

        val logLevel = this.preference.getLogLevel()

        assertThat(logLevel).isEqualTo(LogLevel.WARNING)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
