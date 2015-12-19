package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.ShowFragment

class ShowActivity : BaseActivity() {
    override fun displayHomeAsUp() = true

    override fun getFragment() = ShowFragment()

    override fun getSelectedMenuId() = R.id.menu_shows

    override fun getTitleResourceId() = R.string.show
}
