package com.mgaetan89.showsrage.extension.realm

import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getEpisodes
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetEpisodesTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getEpisodes_existingShow_existingSeason_ascending() {
        this.realm.getEpisodes(72173, 4, false, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(15)

            for (i in 1 until it.size) {
                assertThat(it[i].number > it [i - 1].number).isTrue()
            }
        })
    }

    @Test
    @UiThreadTest
    fun getEpisodes_existingShow_existingSeason_descending() {
        this.realm.getEpisodes(72173, 4, true, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(15)

            for (i in 1 until it.size) {
                assertThat(it[i].number < it [i - 1].number).isTrue()
            }
        })
    }

    @Test
    @UiThreadTest
    fun getEpisodes_existingShow_missingSeason_ascending() {
        this.realm.getEpisodes(72173, 3, false, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isEmpty()
        })
    }

    @Test
    @UiThreadTest
    fun getEpisodes_existingShow_missingSeason_descending() {
        this.realm.getEpisodes(72173, 3, true, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isEmpty()
        })
    }

    @Test
    @UiThreadTest
    fun getEpisodes_missingShow_missingSeason_ascending() {
        this.realm.getEpisodes(42, 3, false, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isEmpty()
        })
    }

    @Test
    @UiThreadTest
    fun getEpisodes_missingShow_missingSeason_descending() {
        this.realm.getEpisodes(42, 3, true, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isEmpty()
        })
    }
}
