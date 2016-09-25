package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.extension.getEpisode
import com.mgaetan89.showsrage.extension.getEpisodes
import com.mgaetan89.showsrage.extension.getRootDirs
import com.mgaetan89.showsrage.extension.getSchedule
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShowStats
import com.mgaetan89.showsrage.extension.getShowWidget
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.getShowsStats
import com.mgaetan89.showsrage.extension.saveEpisode
import com.mgaetan89.showsrage.extension.saveRootDirs
import com.mgaetan89.showsrage.extension.saveShowStat
import com.mgaetan89.showsrage.extension.saveShowWidget
import com.mgaetan89.showsrage.extension.saveShows
import com.mgaetan89.showsrage.extension.saveShowsStat
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStat
import com.mgaetan89.showsrage.model.ShowWidget
import com.mgaetan89.showsrage.model.ShowsStat
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

object RealmManager {
    private var realm: Realm? = null

    fun close() {
        if (this.realm != null && !this.realm!!.isClosed) {
            this.realm!!.close()
            this.realm = null
        }
    }

    fun getEpisode(episodeId: String, listener: RealmChangeListener<Episode>?): Episode? {
        return this.getRealm()?.getEpisode(episodeId, listener)
    }

    fun getEpisodes(episodeId: String, listener: RealmChangeListener<RealmResults<OmDbEpisode>>): RealmResults<OmDbEpisode>? {
        return this.getRealm()?.getEpisodes(episodeId, listener)
    }

    fun getRootDirs() = this.getRealm()?.getRootDirs()

    fun getSchedule(section: String, listener: RealmChangeListener<RealmResults<Schedule>>): RealmResults<Schedule>? {
        return this.getRealm()?.getSchedule(section, listener)
    }

    fun getShow(indexerId: Int): Show? {
        return this.getRealm()?.getShow(indexerId)
    }

    fun getShows(anime: Boolean?, listener: RealmChangeListener<RealmResults<Show>>?): RealmResults<Show>? {
        return this.getRealm()?.getShows(anime, listener)
    }

    fun getShowsStats(listener: RealmChangeListener<RealmResults<ShowsStat>>) = this.getRealm()?.getShowsStats(listener)

    fun getShowStat(indexerId: Int) = this.getRealm()?.getShowStats(indexerId)

    fun getShowWidget(appWidgetId: Int) = this.getRealm()?.getShowWidget(appWidgetId)

    fun init() {
        this.close()

        this.realm = Realm.getDefaultInstance()
    }

    fun saveEpisode(episode: Episode, indexerId: Int, season: Int, episodeNumber: Int) {
        this.getRealm()?.saveEpisode(episode, indexerId, season, episodeNumber)
    }

    fun saveEpisode(episode: OmDbEpisode) = this.getRealm()?.saveEpisode(episode)

    fun saveRootDirs(rootDirs: List<RootDir>) = this.getRealm()?.saveRootDirs(rootDirs)

    fun saveShows(shows: List<Show>) = this.getRealm()?.saveShows(shows)

    fun saveShowsStat(stat: ShowsStat) = this.getRealm()?.saveShowsStat(stat)

    fun saveShowStat(stat: ShowStat, indexerId: Int) = this.getRealm()?.saveShowStat(stat, indexerId) ?: RealmShowStat()

    fun saveShowWidget(showWidget: ShowWidget) = this.getRealm()?.saveShowWidget(showWidget)

    private fun getRealm() = if (this.realm?.isClosed ?: true) null else this.realm
}
