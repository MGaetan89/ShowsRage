package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ScheduleAdapter
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Schedule
import io.realm.RealmChangeListener
import io.realm.RealmResults

class ScheduleSectionFragment : Fragment(), RealmChangeListener<RealmResults<Schedule>> {
    private var adapter: ScheduleAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var schedules: RealmResults<Schedule>? = null

    override fun onChange(schedules: RealmResults<Schedule>) {
        if (this.adapter == null && this.schedules != null) {
            this.adapter = ScheduleAdapter(this.schedules!!)

            this.recyclerView!!.adapter = this.adapter
        }

        if (this.schedules?.isEmpty() ?: true) {
            // We don't have anything in this schedule section
            // So the Fragment is not necessary, and we can just remove it
            this.activity.supportFragmentManager.beginTransaction()
                    .remove(this)
                    .commit();
        }

        this.adapter?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val section = this.arguments?.getString(Constants.Bundle.SCHEDULE_SECTION, "") ?: ""

        this.schedules = RealmManager.getSchedule(section, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_schedule_section, container, false)

        if (view != null) {
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

            if (this.recyclerView != null) {
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)

                this.recyclerView!!.layoutManager = GridLayoutManager(this.activity, columnCount)
            }
        }

        return view
    }

    override fun onDestroy() {
        this.schedules?.removeChangeListeners()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.recyclerView = null

        super.onDestroyView()
    }
}
