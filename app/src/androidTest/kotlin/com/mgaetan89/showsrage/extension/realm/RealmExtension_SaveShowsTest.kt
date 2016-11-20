package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.saveShows
import com.mgaetan89.showsrage.model.Show
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowsTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getShows()).hasSize(83)
    }

    @Test
    fun saveShows() {
        val shows = mutableListOf<Show>()

        for (i in 1..3) {
            shows.add(Show().apply {
                this.indexerId = i
            })
        }

        this.realm.saveShows(shows)

        assertThat(this.getShows()).hasSize(3)
    }

    private fun getShows() = this.realm.getShows(null)
}
