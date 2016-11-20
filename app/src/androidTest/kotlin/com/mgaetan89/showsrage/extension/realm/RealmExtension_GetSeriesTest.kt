package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getSeries
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetSeriesTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getSeries() {
        this.realm.getSeries("tt0176385", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(1)
            assertThat(it.first().imdbId).isEqualTo("tt0176385")
            assertThat(it.first().title).isEqualTo("Pokémon")
        })
    }

    @Test
    @UiThreadTest
    fun getSeries_notFound() {
        this.realm.getSeries("tt0", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(1)
            assertThat(it.first().imdbId).isEqualTo("tt0")
            assertThat(it.first().title).isNull()
        })
    }

    companion object {
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
