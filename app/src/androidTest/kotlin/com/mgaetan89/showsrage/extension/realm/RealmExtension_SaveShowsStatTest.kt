package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveShowsStat
import com.mgaetan89.showsrage.model.ShowsStat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowsStatTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getShowsStats()).hasSize(1)
    }

    @Test
    fun saveShowsStat() {
        this.realm.saveShowsStat(ShowsStat().apply {
            this.episodesDownloaded = 5
            this.episodesSnatched = 10
            this.episodesTotal = 50
            this.showsActive = 20
            this.showsTotal = 25
        })

        val showsStats = this.getShowsStats()
        assertThat(showsStats).hasSize(1)

        val showsStat = showsStats.first()
        assertThat(showsStat).isNotNull()
        assertThat(showsStat.episodesDownloaded).isEqualTo(5)
        assertThat(showsStat.episodesMissing).isEqualTo(35)
        assertThat(showsStat.episodesSnatched).isEqualTo(10)
        assertThat(showsStat.episodesTotal).isEqualTo(50)
        assertThat(showsStat.showsActive).isEqualTo(20)
        assertThat(showsStat.showsTotal).isEqualTo(25)
    }

    private fun getShowsStats() = this.realm.where(ShowsStat::class.java).findAll()
}
