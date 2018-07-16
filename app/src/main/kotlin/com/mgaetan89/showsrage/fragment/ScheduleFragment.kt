package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.SchedulePagerAdapter
import com.mgaetan89.showsrage.extension.clearSchedule
import com.mgaetan89.showsrage.extension.getScheduleSections
import com.mgaetan89.showsrage.extension.saveSchedules
import com.mgaetan89.showsrage.model.Schedules
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_tabbed.swipe_refresh
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class ScheduleFragment : TabbedFragment(), Callback<Schedules> {
	private val sectionIds = mutableListOf<String>()
	private val sectionLabels = mutableListOf<String>()

	override fun failure(error: RetrofitError?) {
		this.swipe_refresh?.isRefreshing = false

		error?.printStackTrace()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val activity = this.activity

		if (activity is MainActivity) {
			activity.displayHomeAsUp(false)
			activity.setTitle(R.string.schedule)
		}

		val sections = Realm.getDefaultInstance().use(Realm::getScheduleSections)

		this.setSections(sections)
		this.onRefresh()
	}

	override fun onDestroy() {
		this.sectionIds.clear()
		this.sectionLabels.clear()

		super.onDestroy()
	}

	override fun onRefresh() {
		this.swipe_refresh?.isRefreshing = true

		SickRageApi.instance.services?.getSchedule(this)
	}

	override fun success(schedules: Schedules?, response: Response?) {
		this.swipe_refresh?.isRefreshing = false

		val data = schedules?.data ?: return

		this.sectionIds.clear()
		this.sectionLabels.clear()

		this.setSections(data.keys.filter {
			data[it]?.isNotEmpty() ?: false
		})

		val realm = Realm.getDefaultInstance()
		realm.clearSchedule()

		data.forEach {
			if (it.value.isNotEmpty()) {
				realm.saveSchedules(it.key, it.value)
			}
		}

		realm.close()
	}

	override fun getAdapter() = SchedulePagerAdapter(this.childFragmentManager, this.sectionIds, this.sectionLabels)

	override fun useSwipeToRefresh() = true

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
		fun getSectionName(sectionId: String?) = when (sectionId?.toLowerCase()) {
			"later" -> R.string.later
			"missed" -> R.string.missed
			"soon" -> R.string.soon
			"today" -> R.string.today
			else -> 0
		}

		fun newInstance() = ScheduleFragment()
	}
}
