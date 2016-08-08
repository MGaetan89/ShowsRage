package com.mgaetan89.showsrage.extension

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.Sort
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetSeasonSortTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getSeasonSort_Ascending() {
        this.preference.edit().putBoolean(Fields.SEASON_SORT.field, true).apply()

        val seasonSort = this.preference.getSeasonSort()

        assertThat(seasonSort).isEqualTo(Sort.ASCENDING)
    }

    @Test
    fun getSeasonSort_Descending() {
        this.preference.edit().putBoolean(Fields.SEASON_SORT.field, false).apply()

        val seasonSort = this.preference.getSeasonSort()

        assertThat(seasonSort).isEqualTo(Sort.DESCENDING)
    }

    @Test
    fun getSeasonSort_Missing() {
        assertThat(this.preference.contains(Fields.SEASON_SORT.field)).isFalse()

        val seasonSort = this.preference.getSeasonSort()

        assertThat(seasonSort).isEqualTo(Sort.DESCENDING)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
