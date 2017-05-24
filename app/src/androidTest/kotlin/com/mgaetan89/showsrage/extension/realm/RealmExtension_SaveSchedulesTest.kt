package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveSchedules
import com.mgaetan89.showsrage.model.Schedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveSchedulesTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getSchedules()).hasSize(36)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()
    }

    @Test
    fun saveSchedules_existingSection() {
        val schedules = (1..5).map {
            Schedule().apply {
                this.airDate = "2016-07-12"
                this.episode = it
                this.indexerId = INDEXER_ID
                this.season = 1
            }
        }

        this.realm.saveSchedules(EXISTING_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(13)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, i, 1, EXISTING_SECTION, "2016-07-12", "")
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
        val schedules = (1..5).map {
            Schedule().apply {
                this.episode = it
                this.episodeName = "Episode name $it"
                this.indexerId = INDEXER_ID
                this.season = 1
            }
        }

        this.realm.saveSchedules(EXISTING_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(13)
        assertThat(this.getSchedules(NEW_SECTION)).isEmpty()

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, i, 1, EXISTING_SECTION, "", "Episode name $i")
        }
    }

    @Test
    fun saveSchedules_newSection() {
        val schedules = (1..5).map {
            Schedule().apply {
                this.airDate = "2016-07-12"
                this.episode = it
                this.indexerId = INDEXER_ID
                this.season = 1
            }
        }

        this.realm.saveSchedules(NEW_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).hasSize(5)

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, i, 1, NEW_SECTION, "2016-07-12", "")
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
        val schedules = (1..5).map {
            Schedule().apply {
                this.episode = it
                this.episodeName = "Episode name $it"
                this.indexerId = INDEXER_ID
                this.season = 1
            }
        }

        this.realm.saveSchedules(NEW_SECTION, schedules)

        assertThat(this.getSchedules()).hasSize(41)
        assertThat(this.getSchedules(EXISTING_SECTION)).hasSize(8)
        assertThat(this.getSchedules(NEW_SECTION)).hasSize(5)

        for (i in 1..5) {
            this.validateSchedule(INDEXER_ID, i, 1, NEW_SECTION, "", "Episode name $i")
        }
    }

    private fun getSchedule(id: String) = this.realm.where(Schedule::class.java).equalTo("id", id).findFirst()

    private fun getSchedules() = this.realm.where(Schedule::class.java).findAll()

    private fun getSchedules(section: String) = this.realm.where(Schedule::class.java).equalTo("section", section).findAll()

    private fun validateSchedule(indexerId: Int, episodeNumber: Int, season: Int, section: String, airDate: String, episodeName: String) {
        val id = "${indexerId}_${season}_$episodeNumber"
        val schedule = this.getSchedule(id)

        assertThat(schedule).isNotNull()
        assertThat(schedule.airDate).isEqualTo(airDate)
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
    }
}
