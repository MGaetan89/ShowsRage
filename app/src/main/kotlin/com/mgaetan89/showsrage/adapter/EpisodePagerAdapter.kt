package com.mgaetan89.showsrage.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.EpisodeDetailFragment

class EpisodePagerAdapter(fragmentManager: FragmentManager?, val fragment: Fragment, val episodes: List<Int>?) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return this.episodes?.size ?: 0
    }

    override fun getItem(position: Int): Fragment? {
        val activity = this.fragment.activity
        val arguments = Bundle()

        if (activity != null) {
            arguments.putAll(activity.intent.extras)
        }

        arguments.putInt(Constants.Bundle.EPISODE_NUMBER, this.episodes?.get(position) ?: 0)

        val fragment = EpisodeDetailFragment()
        fragment.arguments = arguments

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val episode = this.episodes?.get(position) ?: 0

        return this.fragment.getString(R.string.episode_name_short, episode)
    }
}