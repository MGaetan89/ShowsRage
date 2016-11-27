package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.deleteShow
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShowStat
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.Quality
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Show
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_DeleteShowTest : RealmTest() {
    @Before
    fun before() {
        this.validateInitialState()
    }

    @Test
    fun deleteShow() {
        this.realm.deleteShow(INDEXER_ID)

        assertThat(this.getAllEpisodes()).hasSize(1594)
        assertThat(this.getAllQualities()).hasSize(82)
        assertThat(this.getAllScheduled()).hasSize(35)
        assertThat(this.getAllShows()).hasSize(82)
        assertThat(this.getAllShowStats()).hasSize(82)

        assertThat(this.getEpisodes()).hasSize(0)
        assertThat(this.getQuality()).isNull()
        assertThat(this.getSchedule()).hasSize(0)
        assertThat(this.getShow()).isNull()
        assertThat(this.getShowStat()).isNull()
    }

    @Test
    fun deleteShow_unknown() {
        this.realm.deleteShow(-1)

        this.validateInitialState()
    }

    private fun getAllEpisodes() = this.realm.where(Episode::class.java).findAll()

    private fun getAllQualities() = this.realm.where(Quality::class.java).findAll()

    private fun getAllScheduled() = this.realm.where(Schedule::class.java).findAll()

    private fun getAllShows() = this.realm.where(Show::class.java).findAll()

    private fun getAllShowStats() = this.realm.where(RealmShowStat::class.java).findAll()

    private fun getEpisodes() = this.realm.where(Episode::class.java).equalTo("indexerId", INDEXER_ID).findAll()

    private fun getQuality() = this.realm.where(Quality::class.java).equalTo("indexerId", INDEXER_ID).findFirst()

    private fun getSchedule() = this.realm.where(Schedule::class.java).equalTo("indexerId", INDEXER_ID).findAll()

    private fun getShow() = this.realm.getShow(INDEXER_ID)

    private fun getShowStat() = this.realm.getShowStat(INDEXER_ID)

    private fun validateInitialState() {
        assertThat(this.getAllEpisodes()).hasSize(1647)
        assertThat(this.getAllQualities()).hasSize(83)
        assertThat(this.getAllScheduled()).hasSize(36)
        assertThat(this.getAllShows()).hasSize(83)
        assertThat(this.getAllShowStats()).hasSize(83)

        assertThat(this.getEpisodes()).hasSize(53)
        assertThat(this.getQuality()).isNotNull()
        assertThat(this.getSchedule()).hasSize(1)
        assertThat(this.getShow()).isNotNull()
        assertThat(this.getShowStat()).isNotNull()

    }

    companion object {
        private const val INDEXER_ID = 257655
    }
}
