package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.EpisodeFragment

class EpisodeActivity : BaseActivity() {
    override fun displayHomeAsUp() = true

    override fun getFragment() = EpisodeFragment()

    override fun getSelectedMenuId() = R.id.menu_shows

    override fun getTitleResourceId() = R.string.episode
}
