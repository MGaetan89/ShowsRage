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
import com.mgaetan89.showsrage.model.Schedule

class ScheduleSectionFragment : Fragment() {
    private var adapter: ScheduleAdapter? = null
    private var emptyView: TextView? = null
    private var recyclerView: RecyclerView? = null
    private val schedules = mutableListOf <Schedule>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val arguments = this.arguments

        if (arguments != null) {
            val schedules = arguments.getSerializable(Constants.Bundle.SCHEDULES) as Collection<Schedule>?

            if (schedules != null) {
                this.schedules.addAll(schedules)
            }
        }

        if (this.schedules.isEmpty()) {
            this.emptyView?.visibility = View.VISIBLE
            this.recyclerView?.visibility = View.GONE
        } else {
            this.emptyView?.visibility = View.GONE
            this.recyclerView?.visibility = View.VISIBLE
        }

        this.adapter?.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_schedule_section, container, false)

        if (view != null) {
            this.emptyView = view.findViewById(android.R.id.empty) as TextView?
            this.recyclerView = view.findViewById(android.R.id.list) as RecyclerView?

            if (this.recyclerView != null) {
                val columnCount = this.resources.getInteger(R.integer.shows_column_count)

                this.adapter = ScheduleAdapter(this.schedules)

                (this.recyclerView as RecyclerView).adapter = this.adapter
                (this.recyclerView as RecyclerView).layoutManager = GridLayoutManager(this.activity, columnCount)
            }
        }

        return view
    }

    override fun onDestroy() {
        this.schedules.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.emptyView = null
        this.recyclerView = null

        super.onDestroyView()
    }
}
