package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getLastVersionCheckTime
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveLastVersionCheckTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetLastVersionCheckTimeTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun getLastVersionCheckTime() {
        this.preference.saveLastVersionCheckTime(123456789L)

        val lastVersionCheckTime = this.preference.getLastVersionCheckTime()

        assertThat(lastVersionCheckTime).isEqualTo(123456789L)
    }

    @Test
    fun getLanguage_Missing() {
        assertThat(this.preference.contains(Fields.LAST_VERSION_CHECK_TIME.field)).isFalse()

        val lastVersionCheckTime = this.preference.getLastVersionCheckTime()

        assertThat(lastVersionCheckTime).isEqualTo(0L)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
