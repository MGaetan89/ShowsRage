package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getLogsAutoUpdateInterval
import com.mgaetan89.showsrage.extension.getPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetLogsAutoUpdateIntervalTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun getLogsAutoUpdateInterval() {
        this.preference.edit().putString(Fields.LOGS_AUTO_UPDATE_INTERVAL.field, "30").apply()

        val autoUpdateInterval = this.preference.getLogsAutoUpdateInterval()

        assertThat(autoUpdateInterval).isEqualTo(30)
    }

    @Test
    fun getLogsAutoUpdateInterval_Empty() {
        this.preference.edit().putString(Fields.LOGS_AUTO_UPDATE_INTERVAL.field, "").apply()

        val autoUpdateInterval = this.preference.getLogsAutoUpdateInterval()

        assertThat(autoUpdateInterval).isEqualTo(0)
    }

    @Test
    fun getLogsAutoUpdateInterval_Missing() {
        assertThat(this.preference.contains(Fields.LOGS_AUTO_UPDATE_INTERVAL.field)).isFalse()

        val autoUpdateInterval = this.preference.getLogsAutoUpdateInterval()

        assertThat(autoUpdateInterval).isEqualTo(0)
    }

    @Test
    fun getLogsAutoUpdateInterval_Null() {
        this.preference.edit().putString(Fields.LOGS_AUTO_UPDATE_INTERVAL.field, null).apply()

        val autoUpdateInterval = this.preference.getLogsAutoUpdateInterval()

        assertThat(autoUpdateInterval).isEqualTo(0)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
