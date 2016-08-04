package com.mgaetan89.showsrage.extension

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
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
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getLastVersionCheckTime() {
        this.preference.edit().putLong(Fields.LAST_VERSION_CHECK_TIME.field, 123456789L).apply()

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
