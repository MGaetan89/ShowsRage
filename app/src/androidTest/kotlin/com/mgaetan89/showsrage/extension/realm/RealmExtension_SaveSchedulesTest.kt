package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.saveSchedules
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.Schedule
import io.realm.Realm
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveSchedulesTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

        this.realm.isAutoRefresh = false

        assertThat(this.getSchedules()).hasSize(36)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()
    }

    @Test
    fun saveSchedules_existingSection() {
        val schedules = mutableListOf<Schedule>()

        for (i in 1..5) {
            schedules.add(Schedule().apply {
                this.episode = i
                this.indexerId = INDEXER_ID
                this.season = 1
            })
        }

        this.realm.saveSchedules(EXISTING_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(13)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, i, 1, EXISTING_SECTION, "")
        }
    }

    @Test
    fun saveSchedules_existingSection_empty() {
        this.realm.saveSchedules(EXISTING_SECTION, emptyList())

        assertThat(this.getSchedules()).hasSize(36)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()
    }

    @Test
    fun saveSchedules_existingSection_update() {
        // First we save some schedules
        this.saveSchedules_existingSection()

        // Then we perform some updates
        val schedules = mutableListOf<Schedule>()

        for (i in 1..5) {
            schedules.add(Schedule().apply {
                this.episodeName = "Episode name $i"
                this.indexerId = INDEXER_ID
                this.season = 1
            })
        }

        this.realm.saveSchedules(EXISTING_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(13)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, 0, 1, EXISTING_SECTION, "Episode name $i")
        }
    }

    @Test
    fun saveSchedules_newSection() {
        val schedules = mutableListOf<Schedule>()

        for (i in 1..5) {
            schedules.add(Schedule().apply {
                this.episode = i
                this.indexerId = INDEXER_ID
                this.season = 1
            })
        }

        this.realm.saveSchedules(NEW_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).hasSize(5)

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, i, 1, NEW_SECTION, "")
        }
    }

    @Test
    fun saveSchedules_newSection_empty() {
        this.realm.saveSchedules(NEW_SECTION, emptyList())

        assertThat(this.getSchedules()).hasSize(36)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()
    }

    @Test
    fun saveSchedules_newSection_update() {
        // First we save some schedules
        this.saveSchedules_newSection()

        // Then we perform some updates
        val schedules = mutableListOf<Schedule>()

        for (i in 1..5) {
            schedules.add(Schedule().apply {
                this.episodeName = "Episode name $i"
                this.indexerId = INDEXER_ID
                this.season = 1
            })
        }

        this.realm.saveSchedules(NEW_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).hasSize(5)

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, 0, 1, NEW_SECTION, "Episode name $i")
        }
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getSchedule(id: String) = this.realm.where(Schedule::class.java).equalTo("id", id).findFirst()

    private fun getSchedules() = this.realm.where(Schedule::class.java).findAll()

    private fun getSchedules(section: String) = this.realm.where(Schedule::class.java).equalTo("section", section).findAll()

    private fun validateSchedule(indexerId: Int, episodeNumber: Int, season: Int, section: String, episodeName: String) {
        val id = "${indexerId}_${episodeNumber}_$season"
        val schedule = this.getSchedule(id)

        assertThat(schedule).isNotNull()
        assertThat(schedule.episode).isEqualTo(episodeNumber)
        assertThat(schedule.episodeName).isEqualTo(episodeName)
        assertThat(schedule.id).isEqualTo(id)
        assertThat(schedule.indexerId).isEqualTo(indexerId)
        assertThat(schedule.season).isEqualTo(season)
        assertThat(schedule.section).isEqualTo(section)
    }

    companion object {
        private const val EXISTING_SECTION = "soon"
        private const val INDEXER_ID = 280494
        private const val NEW_SECTION = "Monday"

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Looper.myLooper().quit()
        }
    }
}
