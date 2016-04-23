package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.SchedulePagerAdapter
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Schedules
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class ScheduleFragment : TabbedFragment(), Callback<Schedules> {
    private val sectionIds = mutableListOf<String>()
    private val sectionLabels = mutableListOf<String>()

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

        this.setSections(RealmManager.getScheduleSections())

        SickRageApi.instance.services?.getSchedule(this)
    }

    override fun onDestroy() {
        this.sectionIds.clear()
        this.sectionLabels.clear()

        super.onDestroy()
    }

    override fun success(schedules: Schedules?, response: Response?) {
        val data = schedules?.data ?: return

        this.sectionIds.clear()
        this.sectionLabels.clear()

        this.setSections(data.keys.filter {
            data[it]?.isNotEmpty() ?: false
        })

        RealmManager.clearSchedule()

        data.forEach {
            if (it.value.isNotEmpty()) {
                RealmManager.saveSchedules(it.key, it.value)
            }
        }
    }

    override fun getAdapter(): PagerAdapter {
        return SchedulePagerAdapter(this.childFragmentManager, this.sectionIds, this.sectionLabels)
    }

    private fun setSections(sections: List<String>) {
        val statuses = listOf("missed", "today", "soon", "later")

        statuses.forEach {
            if (sections.contains(it)) {
                this.sectionIds.add(it)
                this.sectionLabels.add(if (this.isAdded) {
                    this.getString(getSectionName(it))
                } else {
                    it
                })
            }
        }

        this.updateState(this.sectionIds.isEmpty())
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
