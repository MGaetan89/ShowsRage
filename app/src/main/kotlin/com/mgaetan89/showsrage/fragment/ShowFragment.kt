package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
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
import com.mgaetan89.showsrage.adapter.ShowPagerAdapter
import com.mgaetan89.showsrage.model.Seasons
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class ShowFragment : Fragment(), Callback<Seasons> {
    private var adapter: ShowPagerAdapter? = null
    private val seasons = mutableListOf<Int>()
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = this.activity

        if (activity is MainActivity) {
            activity.displayHomeAsUp(true)
            activity.setTitle(R.string.show)
        }

        val show = this.arguments.getParcelable<Show>(Constants.Bundle.SHOW_MODEL)
        val sort = getSeasonsSort(PreferenceManager.getDefaultSharedPreferences(activity))

        SickRageApi.instance.services?.getSeasons(show.indexerId, sort, this)

        this.tabLayout = activity.findViewById(R.id.tabs) as TabLayout?

        if (this.tabLayout != null && this.viewPager != null) {
            (this.tabLayout as TabLayout).setupWithViewPager(this.viewPager)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_show, container, false)

        if (view != null) {
            this.viewPager = view.findViewById(R.id.show_pager) as ViewPager?

            if (this.viewPager != null ) {
                this.adapter = ShowPagerAdapter(this.childFragmentManager, this, this.seasons)

                (this.viewPager as ViewPager).adapter = this.adapter
            }
        }

        return view
    }

    override fun onDestroy() {
        this.seasons.clear()

        super.onDestroy()
    }

    override fun onDestroyView() {
        this.tabLayout = null
        this.viewPager = null

        super.onDestroyView()
    }

    override fun success(seasons: Seasons?, response: Response?) {
        this.seasons.clear()
        this.seasons.addAll(seasons?.data ?: emptyList())

        this.adapter?.notifyDataSetChanged()

        this.tabLayout?.visibility = if (this.seasons.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        fun getSeasonsSort(preferences: SharedPreferences?): String {
            return if (preferences?.getBoolean("display_seasons_sort", false) ?: false) "asc" else "desc"
        }
    }
}
