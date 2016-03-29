package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.SchedulePagerAdapter
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Schedules
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.util.*

class ScheduleFragment : TabbedFragment(), Callback<Schedules> {
    private val schedules = mutableListOf<ArrayList<Schedule>>()
    private val sections = mutableListOf<String>()

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = this.activity

        if (activity is MainActivity) {
            activity.displayHomeAsUp(false)
            activity.setTitle(R.string.schedule)
        }

        SickRageApi.instance.services?.getSchedule(this)
    }

    override fun onDestroy() {
        this.schedules.clear()
        this.sections.clear()

        super.onDestroy()
    }

    override fun success(schedules: Schedules?, response: Response?) {
        val data = schedules?.data ?: return
        val statuses = listOf("missed", "today", "soon", "later")

        for (status in statuses) {
            if (data.containsKey(status)) {
                val scheduleForStatus = data[status]

                if (scheduleForStatus?.isNotEmpty() ?: false) {
                    this.schedules.add(scheduleForStatus!!)

                    if (this.isAdded) {
                        this.sections.add(this.getString(getSectionName(status)))
                    } else {
                        this.sections.add(status)
                    }
                }
            }
        }

        this.updateState(this.sections.isEmpty())
    }

    override fun getAdapter(): PagerAdapter {
        return SchedulePagerAdapter(this.childFragmentManager, this.sections, this.schedules)
    }

    companion object {
        fun getSectionName(sectionId: String?): Int {
            return when (sectionId?.toLowerCase()) {
                "later" -> R.string.later
                "missed" -> R.string.missed
                "soon" -> R.string.soon
                "today" -> R.string.today
                else -> 0
            }
        }
    }
}
