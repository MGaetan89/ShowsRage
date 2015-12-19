package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.HistoryFragment

class HistoryActivity : BaseActivity() {
    override fun displayHomeAsUp() = false

    override fun getFragment() = HistoryFragment()

    override fun getSelectedMenuId() = R.id.menu_history

    override fun getTitleResourceId() = R.string.history
}
