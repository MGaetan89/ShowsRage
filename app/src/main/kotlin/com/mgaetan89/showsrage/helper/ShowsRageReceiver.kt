package com.mgaetan89.showsrage.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.fragment.AddShowOptionsFragment
import com.mgaetan89.showsrage.fragment.EpisodeFragment
import com.mgaetan89.showsrage.fragment.ShowFragment
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.network.SickRageApi
import java.lang.ref.WeakReference

class ShowsRageReceiver(activity: MainActivity) : BroadcastReceiver() {
    val activityReference: WeakReference<MainActivity>;

    init {
        this.activityReference = WeakReference(activity)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Constants.Intents.ACTION_EPISODE_ACTION_SELECTED -> this.handleEpisodeActionSelected(intent!!)
            Constants.Intents.ACTION_EPISODE_SELECTED -> this.handleEpisodeSelected(intent!!)
            Constants.Intents.ACTION_SEARCH_RESULT_SELECTED -> this.handleSearchResultSelected(intent!!)
            Constants.Intents.ACTION_SHOW_SELECTED -> this.handleShowSelected(intent!!)
        }
    }

    private fun handleEpisodeActionSelected(intent: Intent) {
        val episodeNumber = intent.getIntExtra(Constants.Bundle.EPISODE_NUMBER, 0)
        val indexId = intent.getIntExtra(Constants.Bundle.INDEXER_ID, 0)
        val menuId = intent.getIntExtra(Constants.Bundle.MENU_ID, 0)
        val seasonNumber = intent.getIntExtra(Constants.Bundle.SEASON_NUMBER, 0)
        val status = Episode.getStatusForMenuId(menuId) ?: ""

        when (menuId) {
            R.id.menu_episode_search -> this.searchEpisode(seasonNumber, episodeNumber, indexId)
            R.id.menu_episode_set_status_failed,
            R.id.menu_episode_set_status_ignored,
            R.id.menu_episode_set_status_skipped,
            R.id.menu_episode_set_status_wanted -> this.setEpisodeStatus(seasonNumber, episodeNumber, indexId, status)
        }
    }

    private fun handleEpisodeSelected(intent: Intent) {
        val activity = this.activityReference.get() ?: return

        val arguments = Bundle()
        arguments.putParcelable(Constants.Bundle.EPISODE_MODEL, intent.getParcelableExtra(Constants.Bundle.EPISODE_MODEL))
        arguments.putInt(Constants.Bundle.EPISODE_NUMBER, intent.getIntExtra(Constants.Bundle.EPISODE_NUMBER, 0))
        arguments.putInt(Constants.Bundle.EPISODES_COUNT, intent.getIntExtra(Constants.Bundle.EPISODES_COUNT, 0))
        arguments.putInt(Constants.Bundle.SEASON_NUMBER, intent.getIntExtra(Constants.Bundle.SEASON_NUMBER, 0))
        arguments.putParcelable(Constants.Bundle.SHOW_MODEL, intent.getParcelableExtra(Constants.Bundle.SHOW_MODEL))

        val fragment = EpisodeFragment()
        fragment.arguments = arguments

        activity.supportFragmentManager.beginTransaction()
                .addToBackStack("episode")
                .replace(R.id.content, fragment)
                .commit()
    }

    private fun handleSearchResultSelected(intent: Intent) {
        val activity = this.activityReference.get() ?: return

        val arguments = Bundle()
        arguments.putInt(Constants.Bundle.INDEXER_ID, intent.getIntExtra(Constants.Bundle.INDEXER_ID, 0))

        val fragment = AddShowOptionsFragment()
        fragment.arguments = arguments
        fragment.show(activity.supportFragmentManager, "add_show")
    }

    private fun handleShowSelected(intent: Intent) {
        val activity = this.activityReference.get() ?: return

        val arguments = Bundle()
        arguments.putParcelable(Constants.Bundle.SHOW_MODEL, intent.getParcelableExtra(Constants.Bundle.SHOW_MODEL))

        val fragment = ShowFragment()
        fragment.arguments = arguments

        activity.supportFragmentManager.beginTransaction()
                .addToBackStack("show")
                .replace(R.id.content, fragment)
                .commit()
    }

    private fun searchEpisode(seasonNumber: Int, episodeNumber: Int, indexerId: Int) {
        val activity = this.activityReference.get() ?: return

        Toast.makeText(activity, activity.getString(R.string.episode_search, episodeNumber, seasonNumber), Toast.LENGTH_SHORT).show()

        SickRageApi.instance.services?.searchEpisode(indexerId, seasonNumber, episodeNumber, activity)
    }

    private fun setEpisodeStatus(seasonNumber: Int, episodeNumber: Int, indexerId: Int, status: String) {
        val activity = this.activityReference.get() ?: return

        AlertDialog.Builder(activity)
                .setMessage(R.string.replace_existing_episode)
                .setPositiveButton(R.string.replace, { dialog, which ->
                    SickRageApi.instance.services?.setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 1, status, activity)
                })
                .setNegativeButton(R.string.keep, { dialog, which ->
                    SickRageApi.instance.services?.setEpisodeStatus(indexerId, seasonNumber, episodeNumber, 0, status, activity)
                })
                .show()
    }
}
