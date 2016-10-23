package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.saveShows
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.Show
import io.realm.Realm
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

        this.realm.isAutoRefresh = false

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

    @After
    fun after() {
        this.realm.close()
    }

    private fun getShows() = this.realm.getShows(null)

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
