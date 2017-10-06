package com.mgaetan89.showsrage.fragment

import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import kotlinx.android.synthetic.main.fragment_tabbed.swipe_refresh
import kotlinx.android.synthetic.main.fragment_tabbed.view_pager

abstract class TabbedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
	private var tabLayout: TabLayout? = null

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		this.tabLayout = this.activity.findViewById(R.id.tabs) as TabLayout?

		if (this.tabLayout != null) {
			this.tabLayout!!.tabMode = this.getTabMode()
			this.tabLayout!!.setupWithViewPager(this.view_pager)
		}
	}

	override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater?.inflate(R.layout.fragment_tabbed, container, false)

	override fun onDestroyView() {
		this.tabLayout = null

		super.onDestroyView()
	}

	override fun onRefresh() = Unit

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		this.swipe_refresh?.isEnabled = this.useSwipeToRefresh()
		this.swipe_refresh?.setColorSchemeResources(R.color.accent)
		this.swipe_refresh?.setOnRefreshListener(this)

		this.view_pager?.adapter = this.getAdapter()
	}

	protected abstract fun getAdapter(): PagerAdapter

	protected open fun getTabMode() = TabLayout.MODE_SCROLLABLE

	protected fun selectTab(position: Int) {
		this.tabLayout?.getTabAt(position)?.select()
	}

	protected fun updateState(empty: Boolean) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && this.activity?.isDestroyed != false) {
			return
		}

		this.view_pager?.adapter?.notifyDataSetChanged()

		this.tabLayout?.visibility = if (empty) View.GONE else View.VISIBLE
	}

	protected abstract fun useSwipeToRefresh(): Boolean
}
