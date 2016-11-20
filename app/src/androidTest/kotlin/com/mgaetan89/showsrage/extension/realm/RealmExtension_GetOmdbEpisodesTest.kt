package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getEpisodes
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetOmdbEpisodesTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getEpisodes() {
        this.realm.getEpisodes("tt2193021_4_18", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(1)
            assertThat(it.first().id).isEqualTo("tt2193021_4_18")
            assertThat(it.first().title).isEqualTo("Eleven-Fifty-Nine")
        })
    }

    @Test
    @UiThreadTest
    fun getEpisodes_notFound() {
        this.realm.getEpisodes("0_0_0", RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).hasSize(1)
            assertThat(it.first().id).isEqualTo("0_0_0")
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
