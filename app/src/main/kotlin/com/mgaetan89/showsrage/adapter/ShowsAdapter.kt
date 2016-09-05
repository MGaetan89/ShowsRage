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
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.databinding.AdapterShowsListBinding
import com.mgaetan89.showsrage.helper.DateTimeHelper
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.presenter.ShowPresenter

class ShowsAdapter(val shows: List<Show>, val itemLayoutResource: Int, val ignoreArticles: Boolean) : RecyclerView.Adapter<ShowsAdapter.ViewHolder>(), SectionTitleProvider {
    override fun getItemCount() = this.shows.size

    override fun getSectionTitle(position: Int): String {
        val showName = Utils.getSortableShowName(this.shows[position], this.ignoreArticles)

        return showName.firstOrNull()?.toUpperCase()?.toString() ?: ""
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val show = this.shows[position]

        holder?.binding?.let {
            val presenter = ShowPresenter(show)

            it.setShow(presenter)
            it.setStats(presenter.getShowStat())
        }

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

    override fun onBindViewHolder(holder: ViewHolder?, position: Int, payloads: MutableList<Any>?) {
        if (payloads?.isEmpty() ?: true) {
            this.onBindViewHolder(holder, position)

            return
        }

        if (payloads!!.contains(Constants.Payloads.SHOWS_STATS)) {
            holder?.binding?.let {
                val show = this.shows[position]

                it.setStats(ShowPresenter(show).getShowStat())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.adapter_shows_list, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val binding: AdapterShowsListBinding
        val nextEpisodeDate: TextView?

        init {
            view.setOnClickListener(this)

            this.binding = DataBindingUtil.bind(view)

            if (!this.binding.stub.isInflated) {
                val viewStub = this.binding.stub.viewStub
                viewStub.layoutResource = itemLayoutResource
                viewStub.inflate()
            }

            this.nextEpisodeDate = this.binding.stub.root.findViewById(R.id.show_next_episode_date) as TextView?
        }

        override fun onClick(view: View?) {
            val context = view?.context ?: return
            val show = shows.getOrNull(adapterPosition) ?: return

            with(Intent(Constants.Intents.ACTION_SHOW_SELECTED)) {
                this.putExtra(Constants.Bundle.INDEXER_ID, show.indexerId)

                LocalBroadcastManager.getInstance(context).sendBroadcast(this)
            }
        }
    }
}
