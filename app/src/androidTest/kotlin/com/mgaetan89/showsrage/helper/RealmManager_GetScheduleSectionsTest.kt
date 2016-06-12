package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_GetScheduleSectionsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
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
