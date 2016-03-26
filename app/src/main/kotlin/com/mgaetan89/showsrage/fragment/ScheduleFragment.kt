package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class ScheduleFragment : Fragment(), Callback<Schedules> {
    private var adapter: SchedulePagerAdapter? = null
    private val schedules = mutableListOf<ArrayList<Schedule>>()
    private val sections = mutableListOf<String>()
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

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

        this.tabLayout = this.activity.findViewById(R.id.tabs) as TabLayout?

        if (this.tabLayout != null && this.viewPager != null) {
            (this.tabLayout as TabLayout).setupWithViewPager(this.viewPager)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_schedule, container, false)

        if (view != null) {
            this.viewPager = view.findViewById(R.id.schedule_pager) as ViewPager?

            if (this.viewPager != null) {
                this.adapter = SchedulePagerAdapter(this.childFragmentManager, this.sections, this.schedules)

                (this.viewPager as ViewPager).adapter = this.adapter
            }
        }

        return view
    }

    override fun onDestroy() {
        this.schedules.clear()
        this.sections.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.tabLayout = null
        this.viewPager = null

        super.onDestroyView()
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

        this.adapter?.notifyDataSetChanged()

        this.tabLayout?.visibility = if (this.sections.isEmpty()) View.GONE else View.VISIBLE
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
