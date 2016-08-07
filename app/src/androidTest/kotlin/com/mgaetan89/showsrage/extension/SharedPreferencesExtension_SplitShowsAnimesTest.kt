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
class SharedPreferencesExtension_SplitShowsAnimesTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun splitShowsAnimes_False() {
        this.preference.edit().putBoolean(Fields.SPLIT_SHOWS_ANIMES.field, false).apply()

        val splitShowsAnimes = this.preference.splitShowsAnimes()

        assertThat(splitShowsAnimes).isFalse()
    }

    @Test
    fun splitShowsAnimes_Missing() {
        assertThat(this.preference.contains(Fields.SPLIT_SHOWS_ANIMES.field)).isFalse()

        val splitShowsAnimes = this.preference.splitShowsAnimes()

        assertThat(splitShowsAnimes).isFalse()
    }

    @Test
    fun splitShowsAnimes_True() {
        this.preference.edit().putBoolean(Fields.SPLIT_SHOWS_ANIMES.field, true).apply()

        val splitShowsAnimes = this.preference.splitShowsAnimes()

        assertThat(splitShowsAnimes).isTrue()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
