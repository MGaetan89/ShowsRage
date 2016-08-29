package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmManager_GetScheduleSectionsTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init()
    }

    @Test
    fun getScheduleSectionsSync() {
        val scheduleSections = RealmManager.getScheduleSections()

        this.validateScheduleSections(scheduleSections)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    private fun validateScheduleSections(scheduleSections: List<String>) {
        assertThat(scheduleSections).hasSize(3)
        assertThat(scheduleSections[0]).isEqualTo("soon")
        assertThat(scheduleSections[1]).isEqualTo("later")
        assertThat(scheduleSections[2]).isEqualTo("today")
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
