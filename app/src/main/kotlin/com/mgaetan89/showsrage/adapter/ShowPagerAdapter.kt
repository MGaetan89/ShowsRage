package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.SeasonFragment
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment

class ShowPagerAdapter(
        fragmentManager: FragmentManager?,
        private val fragment: Fragment,
        private val season: List<Int>?
) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        // We have at least a tab for the show overview
        return (this.season?.size ?: 0) + 1
    }

    override fun getItem(position: Int): Fragment? {
        if (position == 0) {
            return ShowOverviewFragment()
        }

        val arguments = Bundle()
        arguments.putInt(Constants.Bundle.SEASON_NUMBER, this.season?.get(position - 1) ?: 0)

        val fragment = SeasonFragment()
        fragment.arguments = arguments

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return this.fragment.getString(R.string.show)
        }

        val season = this.season?.get(position - 1) ?: 0

        if (season == 0) {
            return this.fragment.getString(R.string.specials)
        }

        return this.fragment.getString(R.string.season_number, season)
    }
}
