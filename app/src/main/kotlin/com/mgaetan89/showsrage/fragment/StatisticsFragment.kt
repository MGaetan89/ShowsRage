package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.ShowsStats
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.text.NumberFormat

class StatisticsFragment : DialogFragment(), Callback<ShowsStats> {
    private var episodesDownloaded: TextView? = null
    private var episodesDownloadedBar: View? = null
    private var episodesMissing: TextView? = null
    private var episodesMissingBar: View? = null
    private var episodesSnatched: TextView? = null
    private var episodesSnatchedBar: View? = null
    private var episodesTotal: TextView? = null
    private var progressLayout: LinearLayout? = null
    private var showsActive: TextView? = null
    private var showsTotal: TextView? = null
    private var statisticsLayout: LinearLayout? = null

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SickRageApi.instance.services?.getShowsStats(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_statistics, null)

        if (view != null) {
            this.episodesDownloaded = view.findViewById(R.id.episodes_downloaded) as TextView?
            this.episodesDownloadedBar = view.findViewById(R.id.episodes_downloaded_bar) as View?
            this.episodesMissing = view.findViewById(R.id.episodes_missing) as TextView?
            this.episodesMissingBar = view.findViewById(R.id.episodes_missing_bar) as View?
            this.episodesSnatched = view.findViewById(R.id.episodes_snatched) as TextView?
            this.episodesSnatchedBar = view.findViewById(R.id.episodes_snatched_bar) as View?
            this.episodesTotal = view.findViewById(R.id.episodes_total) as TextView?
            this.progressLayout = view.findViewById(R.id.progress_layout) as LinearLayout?
            this.showsActive = view.findViewById(R.id.shows_active) as TextView?
            this.showsTotal = view.findViewById(R.id.shows_total) as TextView?
            this.statisticsLayout = view.findViewById(R.id.statistics_layout) as LinearLayout?
        }

        val builder = AlertDialog.Builder(this.context, this.theme)
        builder.setTitle(R.string.statistics)
        builder.setView(view)
        builder.setPositiveButton(R.string.close, null)

        return builder.create()
    }

    override fun onDestroyView() {
        this.episodesDownloaded = null
        this.episodesDownloadedBar = null
        this.episodesMissing = null
        this.episodesMissingBar = null
        this.episodesSnatched = null
        this.episodesSnatchedBar = null
        this.episodesTotal = null
        this.progressLayout = null
        this.showsActive = null
        this.showsTotal = null
        this.statisticsLayout = null

        super.onDestroyView()
    }

    override fun success(showsStats: ShowsStats?, response: Response?) {
        val showsStat = showsStats?.data ?: return
        val episodesDownloaded = showsStat.episodesDownloaded
        val episodesMissing = showsStat.episodesMissing
        val episodesSnatched = showsStat.episodesSnatched
        val episodesTotal = showsStat.episodesTotal
        val weightDownloaded = episodesDownloaded.toFloat() / episodesTotal
        val weightMissing = episodesMissing.toFloat() / episodesTotal
        val weightSnatched = episodesSnatched.toFloat() / episodesTotal

        this.episodesDownloaded?.text = this.getString(R.string.downloaded_count, numberFormat.format(episodesDownloaded), getFormattedRatio(episodesDownloaded, episodesTotal))
        this.episodesMissing?.text = this.getString(R.string.missing, numberFormat.format(episodesMissing), getFormattedRatio(episodesMissing, episodesTotal))
        this.episodesSnatched?.text = this.getString(R.string.snatched_count, numberFormat.format(episodesSnatched), getFormattedRatio(episodesSnatched, episodesTotal))
        this.episodesTotal?.text = this.getString(R.string.total, numberFormat.format(episodesTotal))
        this.progressLayout?.visibility = View.GONE
        this.showsActive?.text = this.getString(R.string.active_count, numberFormat.format(showsStat.showsActive))
        this.showsTotal?.text = this.getString(R.string.total, numberFormat.format(showsStat.showsTotal))
        this.statisticsLayout?.visibility = View.VISIBLE

        if (this.episodesDownloadedBar != null) {
            val layoutParams = (this.episodesDownloadedBar as View).layoutParams

            if (layoutParams is LinearLayout.LayoutParams) {
                layoutParams.weight = weightDownloaded
            }
        }

        if (this.episodesMissingBar != null) {
            val layoutParams = (this.episodesMissingBar as View).layoutParams

            if (layoutParams is LinearLayout.LayoutParams) {
                layoutParams.weight = weightMissing
            }
        }

        if (this.episodesSnatchedBar != null) {
            val layoutParams = (this.episodesSnatchedBar as View).layoutParams

            if (layoutParams is LinearLayout.LayoutParams) {
                layoutParams.weight = weightSnatched
            }
        }
    }

    companion object {
        val numberFormat = NumberFormat.getInstance()

        init {
            this.numberFormat.maximumFractionDigits = 1
        }

        fun getFormattedRatio(a: Int, b: Int): String {
            if (b == 0) {
                return "0"
            }


            return this.numberFormat.format(100f * a / b)
        }
    }
}
