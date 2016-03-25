package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.EpisodePagerAdapter

class EpisodeFragment : Fragment() {
    private var adapter: EpisodePagerAdapter? = null
    private val episodes = mutableListOf<Int>()
    private var viewPager: ViewPager? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = this.activity
        val arguments = this.arguments

        if (activity is MainActivity) {
            activity.displayHomeAsUp(true)
            activity.setTitle(R.string.episode)
        }

        if (arguments != null) {
            val episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER, 0)
            val episodesCount = arguments.getInt(Constants.Bundle.EPISODES_COUNT, 0)

            for (i in episodesCount downTo 1) {
                this.episodes.add(i)
            }

            val preferences = PreferenceManager.getDefaultSharedPreferences(activity)
            val ascendingOrder = preferences.getBoolean("display_episodes_sort", false)

            if (ascendingOrder) {
                this.episodes.reverse()
            }

            this.adapter?.notifyDataSetChanged()

            val tabLayout = activity.findViewById(R.id.tabs) as TabLayout?

            if (tabLayout != null && this.viewPager != null) {
                tabLayout.setupWithViewPager(this.viewPager)
                tabLayout.visibility = View.VISIBLE

                tabLayout.getTabAt(if (ascendingOrder) episodeNumber - 1 else episodesCount - episodeNumber)?.select()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_show, container, false)

        if (view != null) {
            this.viewPager = view.findViewById(R.id.show_pager) as ViewPager?

            if (this.viewPager != null) {
                this.adapter = EpisodePagerAdapter(this.childFragmentManager, this, this.episodes)

                (this.viewPager as ViewPager).adapter = this.adapter
            }
        }

        return view
    }

    override fun onDestroy() {
        this.episodes.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.viewPager = null

        super.onDestroyView()
    }
}
