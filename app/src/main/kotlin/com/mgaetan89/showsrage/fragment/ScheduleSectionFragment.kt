package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.adapter.ScheduleAdapter
import com.mgaetan89.showsrage.extension.getSchedule
import com.mgaetan89.showsrage.model.Schedule
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

class ScheduleSectionFragment : Fragment(), RealmChangeListener<RealmResults<Schedule>> {
    private val adapter: ScheduleAdapter by lazy { ScheduleAdapter(this.schedules) }
    private var emptyView: TextView? = null
    private val realm: Realm by lazy { Realm.getDefaultInstance() }
    private var recyclerView: RecyclerView? = null
    private val schedules: RealmResults<Schedule> by lazy {
        this.realm.getSchedule(this.arguments.getString(Constants.Bundle.SCHEDULE_SECTION, ""), this)
    }

    override fun onChange(schedules: RealmResults<Schedule>) {
        if (this.schedules.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_schedule_section, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

            if (this.recyclerView != null) {
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)

                this.recyclerView!!.adapter = this.adapter
                this.recyclerView!!.layoutManager = GridLayoutManager(this.activity, columnCount)
            }
        }

        return view
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null

        super.onDestroyView()
    }

    override fun onStop() {
        if (this.schedules.isValid) {
            this.schedules.removeChangeListeners()
        }

        this.realm.close()

        super.onStop()
    }
}
