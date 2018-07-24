package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.R
import kotlinx.android.synthetic.main.fragment_tabbed.swipe_refresh
import kotlinx.android.synthetic.main.fragment_tabbed.view_pager

abstract class TabbedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
	protected var viewPager: ViewPager? = null
		get() = this.view_pager

	private var tabLayout: TabLayout? = null

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		this.tabLayout = this.activity?.findViewById(R.id.tabs) as TabLayout?

		if (this.tabLayout != null) {
			this.tabLayout!!.tabMode = this.getTabMode()
			this.tabLayout!!.setupWithViewPager(this.viewPager)
		}
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
			= inflater.inflate(R.layout.fragment_tabbed, container, false)

	override fun onDestroyView() {
		this.tabLayout = null

		super.onDestroyView()
	}

	override fun onRefresh() = Unit

	@CallSuper
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		this.swipe_refresh?.let {
			it.isEnabled = this.useSwipeToRefresh()
			it.setColorSchemeResources(R.color.accent)
			it.setOnRefreshListener(this)
		}

		this.viewPager?.adapter = this.getAdapter()
	}

	protected abstract fun getAdapter(): PagerAdapter

	protected open fun getTabMode() = TabLayout.MODE_SCROLLABLE

	protected fun selectTab(position: Int) {
		this.tabLayout?.getTabAt(position)?.select()
	}

	protected fun updateState(empty: Boolean) {
		if (this.activity?.isDestroyed != false) {
			return
		}

		this.viewPager?.adapter?.notifyDataSetChanged()

		this.tabLayout?.visibility = if (empty) View.GONE else View.VISIBLE
	}

	protected abstract fun useSwipeToRefresh(): Boolean
}
