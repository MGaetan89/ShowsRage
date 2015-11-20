package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment
import com.mgaetan89.showsrage.model.Schedule
import java.util.*

class SchedulePagerAdapter(fragmentManager: FragmentManager?, val sections: List<String>?, val schedules: List<ArrayList<Schedule>>?) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return this.sections?.size ?: 0
    }

    override fun getItem(position: Int): Fragment? {
        val arguments = Bundle()
        arguments.putSerializable(Constants.Bundle.SCHEDULES, this.schedules?.get(position))

        val fragment = ScheduleSectionFragment()
        fragment.arguments = arguments

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return this.sections?.get(position) ?: ""
    }
}
