package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.ImageLoader
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.presenter.SchedulePresenter
import io.realm.RealmRecyclerViewAdapter
import io.realm.RealmResults
import kotlinx.android.synthetic.main.adapter_schedule_list_content.view.episode_actions
import kotlinx.android.synthetic.main.adapter_schedule_list_content.view.episode_air_date_time
import kotlinx.android.synthetic.main.adapter_schedule_list_content.view.episode_logo
import kotlinx.android.synthetic.main.adapter_schedule_list_content.view.episode_name
import kotlinx.android.synthetic.main.adapter_schedule_list_content.view.episode_network_quality

class ScheduleAdapter(schedules: RealmResults<Schedule>) : RealmRecyclerViewAdapter<Schedule, ScheduleAdapter.ViewHolder>(schedules, true) {
	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
		val schedule = this.getItem(position).takeIf { it != null && it.isValid } ?: return

		holder?.bind(schedule)
	}

	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
		val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_schedule_list, parent, false)

		return ViewHolder(view)
	}

	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
		private val actions = view.episode_actions
		private val airDateTime = view.episode_air_date_time
		private val logo = view.episode_logo
		private val name = view.episode_name
		private val networkQuality = view.episode_network_quality

		init {
			this.actions.setOnClickListener(this)
			this.name.isSelected = true

			view.setOnClickListener(this)
		}

		fun bind(schedule: Schedule) {
			val context = this.itemView.context
			val presenter = SchedulePresenter(schedule, context)

			this.airDateTime.text = presenter.getAirDateTime()

			this.logo.contentDescription = presenter.getShowName()
			ImageLoader.load(this.logo, presenter.getPosterUrl(), true)

			this.name.text = context.getString(R.string.show_name_episode, presenter.getShowName(), presenter.getSeason(), presenter.getEpisode())
			this.networkQuality.text = context.getString(R.string.separated_texts, presenter.getNetwork(), presenter.getQuality())
		}

		override fun onClick(view: View) {
			when (view.id) {
				R.id.episode_actions -> this.showActions()
				else -> this.showPlot()
			}
		}

		override fun onMenuItemClick(item: MenuItem) = this.notifyEpisodeActionSelected(item)

		private fun notifyEpisodeActionSelected(item: MenuItem): Boolean {
			val schedule = getItem(this.adapterPosition).takeIf { it != null && it.isValid } ?: return false

			Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED).also {
				it.putExtra(Constants.Bundle.EPISODE_NUMBER, schedule.episode)
				it.putExtra(Constants.Bundle.INDEXER_ID, schedule.indexerId)
				it.putExtra(Constants.Bundle.MENU_ID, item.itemId)
				it.putExtra(Constants.Bundle.SEASON_NUMBER, schedule.season)

				LocalBroadcastManager.getInstance(this.itemView.context).sendBroadcast(it)
			}

			return true
		}

		private fun showActions() {
			PopupMenu(this.actions.context, this.actions).also {
				it.inflate(R.menu.episode_action)
				it.setOnMenuItemClickListener(this)
				it.show()
			}
		}

		private fun showPlot() {
			val context = this.itemView.context
			val schedule = getItem(this.adapterPosition).takeIf { it != null && it.isValid } ?: return
			val plot = schedule.episodePlot

			if (!plot.isNullOrEmpty()) {
				var message = context.getString(R.string.season_episode_name, schedule.season, schedule.episode, schedule.episodeName)
				message += "\n\n"
				message += plot

				val dialog = AlertDialog.Builder(context)
						.setTitle(schedule.showName)
						.setMessage(message)
						.setPositiveButton(R.string.close, null)
						.show()

				try {
					val textView = dialog.window.decorView.findViewById(android.R.id.message) as TextView?
					textView?.setTextIsSelectable(true)
				} catch (exception: Exception) {
					exception.printStackTrace()
				}
			} else {
				Toast.makeText(context, R.string.no_plot, Toast.LENGTH_SHORT).show()
			}
		}
	}
}
