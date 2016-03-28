package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R

abstract class TabbedFragment : Fragment() {
    private var adapter: FragmentStatePagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        this.tabLayout = this.activity.findViewById(R.id.tabs) as TabLayout?

        if (this.tabLayout != null && this.viewPager != null) {
            this.tabLayout!!.tabMode = this.getTabMode()
            this.tabLayout!!.setupWithViewPager(this.viewPager)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_tabbed, container, false)

        if (view != null) {
            this.viewPager = view.findViewById(R.id.view_pager) as ViewPager?

            if (this.viewPager != null) {
                this.adapter = this.getAdapter()

                this.viewPager!!.adapter = this.adapter
            }
        }

        return view
    }

    override fun onDestroyView() {
        this.tabLayout = null
        this.viewPager = null

        super.onDestroyView()
    }

    protected abstract fun getAdapter(): FragmentStatePagerAdapter

    protected open fun getTabMode() = TabLayout.MODE_SCROLLABLE

    protected fun selectTab(position: Int) {
        this.tabLayout?.getTabAt(position)?.select()
    }

    protected fun updateState(empty: Boolean) {
        this.adapter?.notifyDataSetChanged()

        this.tabLayout?.visibility = if (empty) View.GONE else View.VISIBLE
    }
}
