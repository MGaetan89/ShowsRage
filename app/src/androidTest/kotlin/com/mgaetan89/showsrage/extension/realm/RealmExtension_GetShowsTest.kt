package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.initRealm
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
class RealmExtension_GetShowsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())
    }

    @Test
    fun getShows_all() {
        val shows = this.realm.getShows(null)

        assertThat(shows).isNotNull()
        assertThat(shows).hasSize(83)
        // TODO Check that the shows are sorted
        //assertThat(shows).isSorted()
    }

    @Test
    fun getShows_anime() {
        val shows = this.realm.getShows(true)

        assertThat(shows).isNotNull()
        assertThat(shows).hasSize(3)
        // TODO Check that the shows are sorted
        //assertThat(shows).isSorted()
    }

    @Test
    fun getShows_show() {
        val shows = this.realm.getShows(false)

        assertThat(shows).isNotNull()
        assertThat(shows).hasSize(80)
        // TODO Check that the shows are sorted
        //assertThat(shows).isSorted()
    }

    @After
    fun after() {
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
