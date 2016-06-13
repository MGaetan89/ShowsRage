package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.Schedule
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_ClearScheduleTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private var schedule: List<Schedule>? = null

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())

        this.schedule = this.getSchedule()

        this.validateNonEmptySchedule(this.schedule)
    }

    @Test
    fun clearSchedule() {
        RealmManager.clearSchedule()

        val schedule = this.getSchedule()

        this.validateEmptySchedule(schedule)
    }

    @After
    fun after() {
        this.schedule?.groupBy({ it.section })?.forEach {
            RealmManager.saveSchedules(it.key, it.value)
        }

        RealmManager.close()
    }

    private fun getSchedule(): List<Schedule>? {
        return RealmManager.getRealm()?.let {
            it.copyFromRealm(it.where(Schedule::class.java).findAll())
        }
    }

    private fun validateEmptySchedule(schedule: List<Schedule>?) {
        assertThat(schedule).isNotNull()
        assertThat(schedule).isEmpty()
    }

    private fun validateNonEmptySchedule(schedule: List<Schedule>?) {
        assertThat(schedule).isNotNull()
        assertThat(schedule).hasSize(11)
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
