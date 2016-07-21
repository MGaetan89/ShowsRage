package com.mgaetan89.showsrage.fragment

import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R

abstract class TabbedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var adapter: PagerAdapter? = null
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
        return inflater?.inflate(R.layout.fragment_tabbed, container, false)
    }

    override fun onDestroyView() {
        this.swipeRefreshLayout = null
        this.tabLayout = null
        this.viewPager = null

        super.onDestroyView()
    }

    override fun onRefresh() = Unit

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (view != null) {
            this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh) as SwipeRefreshLayout?
            this.swipeRefreshLayout?.let {
                it.isEnabled = this.useSwipeToRefresh()
                it.setColorSchemeResources(R.color.accent)
                it.setOnRefreshListener(this)
            }

            this.viewPager = view.findViewById(R.id.view_pager) as ViewPager?

            if (this.viewPager != null) {
                this.adapter = this.getAdapter()

                this.viewPager!!.adapter = this.adapter
            }
        }
    }

    protected abstract fun getAdapter(): PagerAdapter

    protected open fun getTabMode() = TabLayout.MODE_SCROLLABLE

    protected fun selectTab(position: Int) {
        this.tabLayout?.getTabAt(position)?.select()
    }

    protected fun updateState(empty: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && this.activity?.isDestroyed ?: true) {
            return
        }

        this.adapter?.notifyDataSetChanged()

        this.tabLayout?.visibility = if (empty) View.GONE else View.VISIBLE
    }

    protected abstract fun useSwipeToRefresh(): Boolean
}
