package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.ShowPagerAdapter
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getSeasonSort
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.model.Seasons
import com.mgaetan89.showsrage.model.Sort
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
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
        val realm = Realm.getDefaultInstance()
        val show = realm.getShow(indexerId)
        val sort = activity.getPreferences().getSeasonSort()
        val seasons = show?.seasonList?.map { it.value.toInt() } ?: emptyList()

        realm.close()

        this.displaySeasons(if (Sort.ASCENDING.equals(sort)) seasons.sorted() else seasons.sortedDescending())

        SickRageApi.instance.services?.getSeasons(indexerId, sort.label, this)
    }

    override fun onDestroy() {
        this.seasons.clear()

        super.onDestroy()
    }

    override fun success(seasons: Seasons?, response: Response?) {
        this.displaySeasons(seasons?.data)
    }

    override fun getAdapter(): PagerAdapter {
        return ShowPagerAdapter(this.childFragmentManager, this, this.seasons)
    }

    override fun useSwipeToRefresh() = false

    private fun displaySeasons(seasons: Iterable<Int>?) {
        this.seasons.clear()
        this.seasons.addAll(seasons ?: emptyList())

        this.updateState(this.seasons.isEmpty())
    }
}
