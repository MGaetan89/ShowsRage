package com.mgaetan89.showsrage.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment

class SchedulePagerAdapter(fragmentManager: FragmentManager, private val ids: List<String>, private val labels: List<String>) : FragmentStatePagerAdapter(fragmentManager) {
	override fun getCount() = this.ids.size

	override fun getItem(position: Int) = ScheduleSectionFragment.newInstance(this.ids[position])

	override fun getPageTitle(position: Int) = this.labels[position]
}
