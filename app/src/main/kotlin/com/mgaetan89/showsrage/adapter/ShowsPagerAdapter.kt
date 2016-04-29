package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.ShowsSectionFragment

class ShowsPagerAdapter(fragmentManager: FragmentManager, val fragment: Fragment, val splitShowsAnimes: Boolean) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount() = if (this.splitShowsAnimes) 2 else 1

    override fun getItem(position: Int): Fragment? {
        val arguments = Bundle()

        if (this.splitShowsAnimes) {
            arguments.putBoolean(Constants.Bundle.ANIME, position == 1)
        }

        val fragment = ShowsSectionFragment()
        fragment.arguments = arguments

        return fragment
    }

    override fun getItemPosition(`object`: Any?) = POSITION_NONE

    override fun getPageTitle(position: Int): CharSequence? {
        if (!this.fragment.isAdded) {
            return super.getPageTitle(position)
        }

        return when (position) {
            0 -> this.fragment.getString(R.string.shows)
            1 -> this.fragment.getString(R.string.animes)
            else -> super.getPageTitle(position)
        }
    }
}
