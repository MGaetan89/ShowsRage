package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.fragment.ScheduleSectionFragment

class SchedulePagerAdapter(fragmentManager: FragmentManager?, val ids: List<String>, val labels: List<String>) : FragmentStatePagerAdapter(fragmentManager) {
	override fun getCount() = this.ids.size

	override fun getItem(position: Int): Fragment? {
		val arguments = Bundle()
		arguments.putString(Constants.Bundle.SCHEDULE_SECTION, this.ids[position])

		val fragment = ScheduleSectionFragment()
		fragment.arguments = arguments

		return fragment
	}

	override fun getPageTitle(position: Int): CharSequence? {
		return this.labels[position]
	}
}
