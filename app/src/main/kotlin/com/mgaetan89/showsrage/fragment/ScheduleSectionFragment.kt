package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ScheduleAdapter
import com.mgaetan89.showsrage.extension.getSchedule
import com.mgaetan89.showsrage.model.Schedule
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_schedule_section.empty
import kotlinx.android.synthetic.main.fragment_schedule_section.list

class ScheduleSectionFragment : Fragment(), RealmChangeListener<RealmResults<Schedule>> {
	private lateinit var realm: Realm
	private lateinit var schedules: RealmResults<Schedule>

	override fun onChange(schedules: RealmResults<Schedule>) {
		if (this.schedules.isEmpty()) {
			this.empty?.visibility = View.VISIBLE
			this.list?.visibility = View.GONE
		} else {
			this.empty?.visibility = View.GONE
			this.list?.visibility = View.VISIBLE
		}

		this.list?.adapter?.notifyDataSetChanged()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_schedule_section, container, false)

	override fun onStart() {
		super.onStart()

		this.realm = Realm.getDefaultInstance()
		this.schedules = this.realm.getSchedule(this.arguments?.getString(Constants.Bundle.SCHEDULE_SECTION, "") ?: "", this)
		this.list?.adapter = ScheduleAdapter(this.schedules)
	}

	override fun onStop() {
		if (this.schedules.isValid) {
			this.schedules.removeAllChangeListeners()
		}

		this.realm.close()

		super.onStop()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val columnCount = this.resources.getInteger(R.integer.shows_column_count)

		this.list?.layoutManager = GridLayoutManager(this.activity, columnCount)
	}

	companion object {
		fun newInstance(section: String) = ScheduleSectionFragment().apply {
			this.arguments = Bundle().apply {
				this.putString(Constants.Bundle.SCHEDULE_SECTION, section)
			}
		}
	}
}
