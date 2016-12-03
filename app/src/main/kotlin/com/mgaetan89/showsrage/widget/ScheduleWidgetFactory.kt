package com.mgaetan89.showsrage.widget

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.clearSchedule
import com.mgaetan89.showsrage.extension.saveSchedules
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.presenter.SchedulePresenter
import io.realm.Realm

class ScheduleWidgetFactory(context: Context) : ListWidgetFactory<Schedule>(context) {
    override fun getViewAt(position: Int): RemoteViews {
        val schedule = this.getItem(position)
        val presenter = SchedulePresenter(schedule, this.context)
        val logoUrl = presenter.getPosterUrl()

        val views = RemoteViews(this.context.packageName, this.itemLayout)
        views.setTextViewText(R.id.episode_date, presenter.getAirDateTime() ?: this.context.getText(R.string.never))
        views.setContentDescription(R.id.episode_logo, presenter.getShowName())
        views.setTextViewText(R.id.episode_title, this.getEpisodeTitle(presenter))

        if (logoUrl.isEmpty()) {
            views.setViewVisibility(R.id.episode_logo, View.INVISIBLE)
        } else {
            ImageLoader.load(this.context, views, R.id.episode_logo, logoUrl, true)

            views.setViewVisibility(R.id.episode_logo, View.VISIBLE)
        }

        return views
    }

    override fun getItems(): List<Schedule> {
        SickRageApi.instance.services?.getSchedule()?.data?.let {
            val realm = Realm.getDefaultInstance()
            val schedules = mutableListOf<Schedule>()
            realm.clearSchedule()

            it.forEach {
                if (it.value.isNotEmpty()) {
                    schedules.addAll(it.value)

                    realm.saveSchedules(it.key, it.value)
                }
            }

            realm.close()

            schedules.sortBy { it.airDate }

            return schedules
        }

        return emptyList()
    }

    private fun getEpisodeTitle(presenter: SchedulePresenter): String {
        return this.context.getString(R.string.show_name_episode, presenter.getShowName(), presenter.getSeason(), presenter.getEpisode())
    }
}
