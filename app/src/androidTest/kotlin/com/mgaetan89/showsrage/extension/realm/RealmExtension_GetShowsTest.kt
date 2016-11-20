package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShows
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowsTest : RealmTest() {
    @Test
    fun getShows_all() {
        val shows = this.realm.getShows(null)

        assertThat(shows).isNotNull()
        assertThat(shows).hasSize(83)
    }

    @Test
    fun getShows_anime() {
        val shows = this.realm.getShows(true)

        assertThat(shows).isNotNull()
        assertThat(shows).hasSize(3)
    }

    @Test
    fun getShows_show() {
        val shows = this.realm.getShows(false)

        assertThat(shows).isNotNull()
        assertThat(shows).hasSize(80)
    }
}
