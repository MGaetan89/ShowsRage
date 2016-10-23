package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getEpisodeSort
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.model.Sort
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetEpisodeSortTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getEpisodeSort_Ascending() {
        this.preference.edit().putBoolean(Fields.EPISODE_SORT.field, true).apply()

        val episodeSort = this.preference.getEpisodeSort()

        assertThat(episodeSort).isEqualTo(Sort.ASCENDING)
    }

    @Test
    fun getEpisodeSort_Descending() {
        this.preference.edit().putBoolean(Fields.EPISODE_SORT.field, false).apply()

        val episodeSort = this.preference.getEpisodeSort()

        assertThat(episodeSort).isEqualTo(Sort.DESCENDING)
    }

    @Test
    fun getEpisodeSort_Missing() {
        assertThat(this.preference.contains(Fields.EPISODE_SORT.field)).isFalse()

        val episodeSort = this.preference.getEpisodeSort()

        assertThat(episodeSort).isEqualTo(Sort.DESCENDING)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
