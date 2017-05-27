package com.mgaetan89.showsrage.fragment

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.adapter.EpisodePagerAdapter
import com.mgaetan89.showsrage.extension.getEpisodeSort
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.model.Sort

class EpisodeFragment : TabbedFragment() {
	private val episodes = mutableListOf<Int>()

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val activity = this.activity
		val arguments = this.arguments

		if (activity is MainActivity) {
			activity.displayHomeAsUp(true)
			activity.setTitle(R.string.episode)
		}

		if (arguments != null) {
			val episodeNumber = arguments.getInt(Constants.Bundle.EPISODE_NUMBER)
			val episodesCount = arguments.getInt(Constants.Bundle.EPISODES_COUNT)

			for (i in episodesCount downTo 1) {
				this.episodes.add(i)
			}

			val ascendingOrder = Sort.ASCENDING == activity.getPreferences().getEpisodeSort()

			if (ascendingOrder) {
				this.episodes.reverse()
			}

			this.updateState(false)
			this.selectTab(if (ascendingOrder) episodeNumber - 1 else episodesCount - episodeNumber)
		}
	}

	override fun onDestroy() {
		this.episodes.clear()

		super.onDestroy()
	}

	override fun getAdapter(): PagerAdapter {
		return EpisodePagerAdapter(this.childFragmentManager, this, this.episodes)
	}

	override fun useSwipeToRefresh() = false
}
