package com.mgaetan89.showsrage.adapter

import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterShowsListBinding
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.presenter.ShowPresenter

class ShowsAdapter(val shows: List<Show>, val itemLayoutResource: Int) : RecyclerView.Adapter<ShowsAdapter.ViewHolder>() {
    override fun getItemCount() = this.shows.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val show = this.shows[position]

        holder?.bind(ShowPresenter(show))

        if (holder?.nextEpisodeDate != null && show.isValid) {
            val nextEpisodeAirDate = show.nextEpisodeAirDate

            if (nextEpisodeAirDate.isNullOrEmpty()) {
                val status = show.getStatusTranslationResource()

                holder?.nextEpisodeDate.text = if (status != 0) {
                    holder?.nextEpisodeDate.resources.getString(status)
                } else {
                    show.status
                }
            } else {
                holder?.nextEpisodeDate.text = DateTimeHelper.getRelativeDate(nextEpisodeAirDate, "yyyy-MM-dd", DateUtils.DAY_IN_MILLIS)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_shows_list, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val nextEpisodeDate: TextView?
        private val binding: AdapterShowsListBinding

        init {
            view.setOnClickListener(this)

            this.binding = DataBindingUtil.bind(view)

            if (!this.binding.stub.isInflated()) {
                val viewStub = this.binding.stub.getViewStub()
                viewStub.setLayoutResource(itemLayoutResource)
                viewStub.inflate()
            }

            this.nextEpisodeDate = this.binding.stub.getRoot().findViewById(R.id.show_next_episode_date) as TextView?
        }

        fun bind(show: ShowPresenter) {
            this.binding.setShow(show)
        }

        override fun onClick(view: View?) {
            val context = view?.context ?: return

            with(Intent(Constants.Intents.ACTION_SHOW_SELECTED)) {
                putExtra(Constants.Bundle.INDEXER_ID, shows[adapterPosition].indexerId)

                LocalBroadcastManager.getInstance(context).sendBroadcast(this)
            }
        }
    }
}
