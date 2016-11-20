package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShow
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowAsyncTest : RealmTest() {
    @Test
    @UiThreadTest
    fun getShow() {
        this.realm.getShow(292174, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it.indexerId).isEqualTo(292174292174)
            assertThat(it.showName).isEqualTo("Dark Matter")
        })
    }

    @Test
    @UiThreadTest
    fun getShow_notFound() {
        this.realm.getShow(0, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it.indexerId).isEqualTo(0)
            assertThat(it.showName).isEqualTo("")
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
