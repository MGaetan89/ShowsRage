package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.ScheduleFragment

class ScheduleActivity : BaseActivity() {
    override fun displayHomeAsUp() = false

    override fun getFragment() = ScheduleFragment()

    override fun getSelectedMenuId() = R.id.menu_schedule

    override fun getTitleResourceId() = R.string.schedule
}
