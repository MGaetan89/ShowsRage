package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.PagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.ShowPagerAdapter
import com.mgaetan89.showsrage.helper.RealmManager
import com.mgaetan89.showsrage.model.Seasons
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class ShowFragment : TabbedFragment(), Callback<Seasons> {
    private val seasons = mutableListOf<Int>()

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

        val indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID)
        val show = RealmManager.getShow(indexerId)
        val sort = getSeasonsSort(PreferenceManager.getDefaultSharedPreferences(activity))

        SickRageApi.instance.services?.getSeasons(show?.indexerId ?: 0, sort, this)
    }

    override fun onDestroy() {
        this.seasons.clear()

        super.onDestroy()
    }

    override fun success(seasons: Seasons?, response: Response?) {
        this.seasons.clear()
        this.seasons.addAll(seasons?.data ?: emptyList())

        this.updateState(this.seasons.isEmpty())
    }

    override fun getAdapter(): PagerAdapter {
        return ShowPagerAdapter(this.childFragmentManager, this, this.seasons)
    }

    companion object {
        fun getSeasonsSort(preferences: SharedPreferences?): String {
            return if (preferences?.getBoolean("display_seasons_sort", false) ?: false) "asc" else "desc"
        }
    }
}
