package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.ShowsSectionFragment
import com.mgaetan89.showsrage.model.Show
import java.util.*

class ShowsPagerAdapter(fragmentManager: FragmentManager, val fragment: Fragment, val shows: Map<Int, List<Show>>) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount() = this.shows.size

    override fun getItem(position: Int): Fragment? {
        val shows = ArrayList<Show>()
        shows.addAll(this.shows.getOrElse(position, { emptyList() }))

        val arguments = Bundle()
        arguments.putSerializable(Constants.Bundle.SHOWS, shows)

        val fragment = ShowsSectionFragment()
        fragment.arguments = arguments

        return fragment
    }

    override fun getItemPosition(`object`: Any?) = POSITION_NONE

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> this.fragment.getString(R.string.shows)
            1 -> this.fragment.getString(R.string.animes)
            else -> super.getPageTitle(position)
        }
    }
}