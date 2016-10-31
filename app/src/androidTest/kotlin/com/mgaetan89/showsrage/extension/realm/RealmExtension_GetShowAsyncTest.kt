package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShow
import io.realm.Realm
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowAsyncTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

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

    @After
    fun after() {
        this.realm.isAutoRefresh = false
        this.realm.close()
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
