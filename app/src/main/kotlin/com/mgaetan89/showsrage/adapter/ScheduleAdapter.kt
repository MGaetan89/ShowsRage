package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterScheduleListBinding
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.presenter.SchedulePresenter

class ScheduleAdapter(val schedules: List<Schedule>) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    override fun getItemCount() = this.schedules.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val schedule = this.schedules[position]

        holder?.bind(SchedulePresenter(schedule, holder.itemView?.context))
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_schedule_list, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private val actions: ImageView?
        private val binding: AdapterScheduleListBinding

        init {
            view.setOnClickListener(this)

            this.binding = DataBindingUtil.bind(view)

            this.actions = this.binding.includeContent.episodeActions
            this.actions?.setOnClickListener(this)
        }

        fun bind(schedule: SchedulePresenter) {
            this.binding.setSchedule(schedule)
        }

        override fun onClick(view: View?) {
            val context = view?.context ?: return

            if (view?.id == R.id.episode_actions) {
                with(PopupMenu(context, this.actions)) {
                    inflate(R.menu.episode_action)
                    setOnMenuItemClickListener(this@ViewHolder)
                    show()
                }
            } else {
                val schedule = schedules[adapterPosition]
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
                    } catch(exception: Exception) {
                        exception.printStackTrace()
                        // The TextView was not found
                    }
                } else {
                    Toast.makeText(context, R.string.no_plot, Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            val context = this.actions?.context ?: return false
            val schedule = schedules[adapterPosition]

            with(Intent(Constants.Intents.ACTION_EPISODE_ACTION_SELECTED)) {
                putExtra(Constants.Bundle.EPISODE_NUMBER, schedule.episode)
                putExtra(Constants.Bundle.INDEXER_ID, schedule.indexerId)
                putExtra(Constants.Bundle.MENU_ID, item?.itemId)
                putExtra(Constants.Bundle.SEASON_NUMBER, schedule.season)

                LocalBroadcastManager.getInstance(context).sendBroadcast(this)
            }

            return true
        }
    }
}
