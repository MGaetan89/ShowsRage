package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.SeasonFragment
import com.mgaetan89.showsrage.fragment.ShowOverviewFragment

class ShowPagerAdapter(fragmentManager: FragmentManager, val fragment: Fragment, val seasons: List<Int>) : FragmentPagerAdapter(fragmentManager) {
    // We have at least a tab for the show overview
    override fun getCount() = this.seasons.size + 1

    override fun getItem(position: Int): Fragment? {
        if (position == 0) {
            val fragment = ShowOverviewFragment()
            fragment.arguments = this.fragment.arguments

            return fragment
        }

        val arguments = Bundle(this.fragment.arguments)
        arguments.putInt(Constants.Bundle.SEASON_NUMBER, this.seasons[position - 1])

        val fragment = SeasonFragment()
        fragment.arguments = arguments

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position == 0) {
            return this.fragment.getString(R.string.show)
        }

        val season = this.seasons[position - 1]

        if (season == 0) {
            return this.fragment.getString(R.string.specials)
        }

        return this.fragment.getString(R.string.season_number, season)
    }
}
