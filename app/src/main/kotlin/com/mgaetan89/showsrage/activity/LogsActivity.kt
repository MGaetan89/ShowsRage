package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.LogsFragment

class LogsActivity : BaseActivity() {
    override fun displayHomeAsUp() = false

    override fun getFragment() = LogsFragment()

    override fun getSelectedMenuId() = R.id.menu_logs

    override fun getTitleResourceId() = R.string.logs
}
