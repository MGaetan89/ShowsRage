package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getShowsFilterStatus
import com.mgaetan89.showsrage.extension.saveShowsFilter
import com.mgaetan89.showsrage.model.ShowsFilters
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetShowsFilterStatusTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getShowsFilterStatus() {
        this.preference.edit().putInt(Fields.SHOW_FILTER_STATUS.field, 42).apply()

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(42)
    }

    @Test
    fun getShowsFilterStatus_Continuing() {
        val status = ShowsFilters.Status.CONTINUING.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @Test
    fun getShowsFilterStatus_Continuing_Ended() {
        val status = ShowsFilters.Status.CONTINUING.status or ShowsFilters.Status.ENDED.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @Test
    fun getShowsFilterStatus_Continuing_Ended_Unknown() {
        val status = ShowsFilters.Status.CONTINUING.status or ShowsFilters.Status.ENDED.status or ShowsFilters.Status.UNKNOWN.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @Test
    fun getShowsFilterStatus_Continuing_Unknown() {
        val status = ShowsFilters.Status.CONTINUING.status or ShowsFilters.Status.UNKNOWN.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @Test
    fun getShowsFilterStatus_Ended() {
        val status = ShowsFilters.Status.ENDED.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @Test
    fun getShowsFilterStatus_Ended_Unknown() {
        val status = ShowsFilters.Status.ENDED.status or ShowsFilters.Status.UNKNOWN.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @Test
    fun getShowsFilterStatus_Missing() {
        assertThat(this.preference.contains(Fields.SHOW_FILTER_STATUS.field)).isFalse()

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(ShowsFilters.Status.ALL.status)
    }

    @Test
    fun getShowsFilterStatus_Unknown() {
        val status = ShowsFilters.Status.UNKNOWN.status

        this.preference.saveShowsFilter(ShowsFilters.State.ALL, status)

        val showsFilterStatus = this.preference.getShowsFilterStatus()

        assertThat(showsFilterStatus).isEqualTo(status)
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
