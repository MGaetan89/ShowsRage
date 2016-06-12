package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.Schedule
import io.realm.RealmChangeListener
import io.realm.RealmResults
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_GetScheduleTest {
    private var scheduleLaterAsync: RealmResults<Schedule>? = null
    private val scheduleLaterAsyncListener = RealmChangeListener<RealmResults<Schedule>> {
        this.scheduleLaterAsync?.removeChangeListeners()

        this.validateScheduleLater(it)
    }

    private var scheduleSoonAsync: RealmResults<Schedule>? = null
    private val scheduleSoonAsyncListener = RealmChangeListener<RealmResults<Schedule>> {
        this.scheduleSoonAsync?.removeChangeListeners()

        this.validateScheduleSoon(it)
    }

    private var scheduleTodayAsync: RealmResults<Schedule>? = null
    private val scheduleTodayAsyncListener = RealmChangeListener<RealmResults<Schedule>> {
        this.scheduleTodayAsync?.removeChangeListeners()

        this.validateScheduleToday(it)
    }

    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    fun getScheduleLaterAsync() {
        this.scheduleLaterAsync = RealmManager.getSchedule("later", this.scheduleLaterAsyncListener)
    }

    @Test
    fun getScheduleSoonAsync() {
        this.scheduleSoonAsync = RealmManager.getSchedule("soon", this.scheduleSoonAsyncListener)
    }

    @Test
    fun getScheduleTodayAsync() {
        this.scheduleTodayAsync = RealmManager.getSchedule("today", this.scheduleTodayAsyncListener)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    private fun validateScheduleLater(schedules: RealmResults<Schedule>?) {
        assertThat(schedules).isNotNull()
        assertThat(schedules).hasSize(7)

        for (i in 1..schedules!!.size - 1) {
            val previous = schedules[i - 1].airDate
            val current = schedules[i].airDate

            if (previous < current) {
                Assertions.fail("Schedule is not properly sorted: %s < %s!".format(previous, current))
            }
        }
    }

    private fun validateScheduleSoon(schedules: RealmResults<Schedule>?) {
        assertThat(schedules).isNotNull()
        assertThat(schedules).hasSize(3)

        for (i in 1..schedules!!.size - 1) {
            val previous = schedules[i - 1].airDate
            val current = schedules[i].airDate

            if (previous < current) {
                Assertions.fail("Schedule is not properly sorted: %s < %s!".format(previous, current))
            }
        }
    }

    private fun validateScheduleToday(schedules: RealmResults<Schedule>?) {
        assertThat(schedules).isNotNull()
        assertThat(schedules).hasSize(1)

        for (i in 1..schedules!!.size - 1) {
            val previous = schedules[i - 1].airDate
            val current = schedules[i].airDate

            if (previous < current) {
                Assertions.fail("Schedule is not properly sorted: %s < %s!".format(previous, current))
            }
        }
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
