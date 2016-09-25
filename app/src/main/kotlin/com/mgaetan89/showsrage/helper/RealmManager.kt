package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.extension.clearHistory
import com.mgaetan89.showsrage.extension.clearSchedule
import com.mgaetan89.showsrage.extension.deleteShow
import com.mgaetan89.showsrage.extension.deleteShowWidget
import com.mgaetan89.showsrage.extension.getEpisode
import com.mgaetan89.showsrage.extension.getEpisodes
import com.mgaetan89.showsrage.extension.getHistory
import com.mgaetan89.showsrage.extension.getLogs
import com.mgaetan89.showsrage.extension.getLogsGroup
import com.mgaetan89.showsrage.extension.getRootDirs
import com.mgaetan89.showsrage.extension.getSchedule
import com.mgaetan89.showsrage.extension.getScheduleSections
import com.mgaetan89.showsrage.extension.getSeries
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShowStats
import com.mgaetan89.showsrage.extension.getShowWidget
import com.mgaetan89.showsrage.extension.getShows
import com.mgaetan89.showsrage.extension.getShowsStats
import com.mgaetan89.showsrage.extension.saveEpisode
import com.mgaetan89.showsrage.extension.saveEpisodes
import com.mgaetan89.showsrage.extension.saveHistory
import com.mgaetan89.showsrage.extension.saveLogs
import com.mgaetan89.showsrage.extension.saveRootDirs
import com.mgaetan89.showsrage.extension.saveSchedules
import com.mgaetan89.showsrage.extension.saveSerie
import com.mgaetan89.showsrage.extension.saveShow
import com.mgaetan89.showsrage.extension.saveShowStat
import com.mgaetan89.showsrage.extension.saveShowWidget
import com.mgaetan89.showsrage.extension.saveShows
import com.mgaetan89.showsrage.extension.saveShowsStat
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Serie
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStat
import com.mgaetan89.showsrage.model.ShowWidget
import com.mgaetan89.showsrage.model.ShowsStat
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults

object RealmManager {
    private var realm: Realm? = null

    fun clearHistory() = this.getRealm()?.clearHistory()

    fun clearSchedule() = this.getRealm()?.clearSchedule()

    fun close() {
        if (this.realm != null && !this.realm!!.isClosed) {
            this.realm!!.close()
            this.realm = null
        }
    }

    fun deleteShow(indexerId: Int) = this.getRealm()?.deleteShow(indexerId)

    fun deleteShowWidget(widgetId: Int) = this.getRealm()?.deleteShowWidget(widgetId)

    fun getEpisode(episodeId: String, listener: RealmChangeListener<Episode>?): Episode? {
        return this.getRealm()?.getEpisode(episodeId, listener)
    }

    fun getEpisodes(episodeId: String, listener: RealmChangeListener<RealmResults<OmDbEpisode>>): RealmResults<OmDbEpisode>? {
        return this.getRealm()?.getEpisodes(episodeId, listener)
    }

    fun getEpisodes(indexerId: Int, season: Int, reversedOrder: Boolean, listener: RealmChangeListener<RealmResults<Episode>>): RealmResults<Episode>? {
        return this.getRealm()?.getEpisodes(indexerId, season, reversedOrder, listener)
    }

    fun getHistory(listener: RealmChangeListener<RealmResults<History>>) = this.getRealm()?.getHistory(listener)

    fun getLogs(logLevel: LogLevel, groups: Array<String>?, listener: RealmChangeListener<RealmResults<LogEntry>>): RealmResults<LogEntry>? {
        return this.getRealm()?.getLogs(logLevel, groups, listener)
    }

    fun getLogsGroup(): List<String> = this.getRealm()?.getLogsGroup() ?: emptyList()

    fun getRootDirs() = this.getRealm()?.getRootDirs()

    fun getSchedule(section: String, listener: RealmChangeListener<RealmResults<Schedule>>): RealmResults<Schedule>? {
        return this.getRealm()?.getSchedule(section, listener)
    }

    fun getScheduleSections() = this.getRealm()?.getScheduleSections() ?: emptyList()

    fun getSeries(imdbId: String, listener: RealmChangeListener<RealmResults<Serie>>): RealmResults<Serie>? {
        return this.getRealm()?.getSeries(imdbId, listener)
    }

    fun getShow(indexerId: Int, listener: RealmChangeListener<Show>? = null): Show? {
        return this.getRealm()?.getShow(indexerId, listener)
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

    fun saveEpisodes(episodes: List<Episode>, indexerId: Int, season: Int) {
        this.getRealm()?.saveEpisodes(episodes, indexerId, season)
    }

    fun saveHistory(histories: List<History>) = this.getRealm()?.saveHistory(histories)

    fun saveLogs(logs: List<LogEntry>, logLevel: LogLevel) = this.getRealm()?.saveLogs(logLevel, logs)

    fun saveRootDirs(rootDirs: List<RootDir>) = this.getRealm()?.saveRootDirs(rootDirs)

    fun saveSchedules(section: String, schedules: List<Schedule>) = this.getRealm()?.saveSchedules(section, schedules)

    fun saveSerie(serie: Serie) = this.getRealm()?.saveSerie(serie)

    fun saveShow(show: Show) = this.getRealm()?.saveShow(show)

    fun saveShows(shows: List<Show>) = this.getRealm()?.saveShows(shows)

    fun saveShowsStat(stat: ShowsStat) = this.getRealm()?.saveShowsStat(stat)

    fun saveShowStat(stat: ShowStat, indexerId: Int) = this.getRealm()?.saveShowStat(stat, indexerId) ?: RealmShowStat()

    fun saveShowWidget(showWidget: ShowWidget) = this.getRealm()?.saveShowWidget(showWidget)

    private fun getRealm() = if (this.realm?.isClosed ?: true) null else this.realm
}
