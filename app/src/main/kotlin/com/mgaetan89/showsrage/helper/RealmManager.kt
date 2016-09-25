package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.extension.clearHistory
import com.mgaetan89.showsrage.extension.clearSchedule
import com.mgaetan89.showsrage.extension.deleteShow
import com.mgaetan89.showsrage.extension.getEpisode
import com.mgaetan89.showsrage.extension.getEpisodes
import com.mgaetan89.showsrage.extension.getHistory
import com.mgaetan89.showsrage.extension.getLogs
import com.mgaetan89.showsrage.extension.getLogsGroup
import com.mgaetan89.showsrage.extension.getRootDirs
import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.Quality
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.RealmString
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Serie
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowStat
import com.mgaetan89.showsrage.model.ShowWidget
import com.mgaetan89.showsrage.model.ShowsStat
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmResults

object RealmManager {
    private var realm: Realm? = null

    fun clearHistory() {
        this.getRealm()?.clearHistory()
    }

    fun clearSchedule() {
        this.getRealm()?.clearSchedule()
    }

    fun close() {
        if (this.realm != null && !this.realm!!.isClosed) {
            this.realm!!.close()
            this.realm = null
        }
    }

    fun deleteShow(indexerId: Int) {
        this.getRealm()?.deleteShow(indexerId)
    }

    fun deleteShowWidget(widgetId: Int) {
        this.getRealm()?.executeTransaction {
            it.where(ShowWidget::class.java)
                    .equalTo("widgetId", widgetId)
                    .findFirst()?.deleteFromRealm()
        }
    }

    fun getEpisode(episodeId: String, listener: RealmChangeListener<Episode>?): Episode? {
        return this.getRealm()?.getEpisode(episodeId, listener)
    }

    fun getEpisodes(episodeId: String, listener: RealmChangeListener<RealmResults<OmDbEpisode>>): RealmResults<OmDbEpisode>? {
        return this.getRealm()?.getEpisodes(episodeId, listener)
    }

    fun getEpisodes(indexerId: Int, season: Int, reversedOrder: Boolean, listener: RealmChangeListener<RealmResults<Episode>>): RealmResults<Episode>? {
        return this.getRealm()?.getEpisodes(indexerId, season, reversedOrder, listener)
    }

    fun getHistory(listener: RealmChangeListener<RealmResults<History>>): RealmResults<History>? {
        return this.getRealm()?.getHistory(listener)
    }

    fun getLogs(logLevel: LogLevel, groups: Array<String>?, listener: RealmChangeListener<RealmResults<LogEntry>>): RealmResults<LogEntry>? {
        return this.getRealm()?.getLogs(logLevel, groups, listener)
    }

    fun getLogsGroup(): List<String> {
        return this.getRealm()?.getLogsGroup() ?: emptyList()
    }

    fun getRootDirs(): RealmResults<RootDir>? {
        return this.getRealm()?.getRootDirs()
    }

    fun getSchedule(section: String, listener: RealmChangeListener<RealmResults<Schedule>>): RealmResults<Schedule>? {
        val realm = this.getRealm() ?: return null
        val schedule = realm.where(Schedule::class.java)
                .equalTo("section", section)
                .findAllSortedAsync("airDate")
        schedule.addChangeListener(listener)

        return schedule
    }

    fun getScheduleSections(): List<String> {
        val realm = this.getRealm() ?: return emptyList()

        return realm.where(Schedule::class.java)
                .distinct("section")
                .map { it.section }
    }

    fun getSeries(imdbId: String, listener: RealmChangeListener<RealmResults<Serie>>): RealmResults<Serie>? {
        val realm = this.getRealm() ?: return null
        val series = realm.where(Serie::class.java).equalTo("imdbId", imdbId).findAllAsync()
        series.addChangeListener(listener)

        return series
    }

    fun getShow(indexerId: Int, listener: RealmChangeListener<Show>? = null): Show? {
        val realm = this.getRealm() ?: return null
        val query = realm.where(Show::class.java).equalTo("indexerId", indexerId)

        if (listener == null) {
            return query.findFirst()
        }

        val show = query.findFirstAsync()
        show.addChangeListener(listener)

        return show
    }

    fun getShows(anime: Boolean?, listener: RealmChangeListener<RealmResults<Show>>?): RealmResults<Show>? {
        val realm = this.getRealm() ?: return null
        val query = realm.where(Show::class.java)

        if (anime != null) {
            query.equalTo("anime", if (anime) 1 else 0)
        }

        if (listener == null) {
            return query.findAllSorted("showName")
        }

        val shows = query.findAllSortedAsync("showName")
        shows.addChangeListener(listener)

        return shows
    }

    fun getShowsStats(listener: RealmChangeListener<RealmResults<ShowsStat>>): RealmResults<ShowsStat>? {
        // There might be no data yet. So we request all data to be sure to always received a valid object.
        val realm = this.getRealm() ?: return null
        val stats = realm.where(ShowsStat::class.java).findAllAsync()
        stats.addChangeListener(listener)

        return stats
    }

    fun getShowStat(indexerId: Int): RealmShowStat? {
        val realm = this.getRealm() ?: return null

        return realm.where(RealmShowStat::class.java)
                .equalTo("indexerId", indexerId)
                .findFirst()
    }

    fun getShowWidget(appWidgetId: Int): ShowWidget? {
        val realm = this.getRealm() ?: return null

        return realm.where(ShowWidget::class.java)
                .equalTo("widgetId", appWidgetId)
                .findFirst()
    }

    fun init() {
        this.close()

        this.realm = Realm.getDefaultInstance()
    }

    fun saveEpisode(episode: Episode, indexerId: Int, season: Int, episodeNumber: Int) {
        this.getRealm()?.executeTransaction {
            this.prepareEpisodeForSaving(episode, indexerId, season, episodeNumber)

            it.copyToRealmOrUpdate(episode)
        }
    }

    fun saveEpisode(episode: OmDbEpisode) {
        this.getRealm()?.executeTransaction {
            this.prepareEpisodeForSaving(episode)

            it.copyToRealmOrUpdate(episode)
        }
    }

    fun saveEpisodes(episodes: List<Episode>, indexerId: Int, season: Int) {
        this.getRealm()?.executeTransaction {
            episodes.forEach {
                this.prepareEpisodeForSaving(it, indexerId, season, it.number)
            }

            it.copyToRealmOrUpdate(episodes)
        }
    }

    fun saveHistory(histories: List<History>) {
        this.clearHistory()

        this.getRealm()?.executeTransaction {
            histories.forEach {
                this.prepareHistoryForSaving(it)
            }

            it.copyToRealmOrUpdate(histories)
        }
    }

    fun saveLogs(logs: List<LogEntry>, logLevel: LogLevel) {
        this.getRealm()?.executeTransaction {
            // Remove all existing logs for the level we are about to save
            it.where(LogEntry::class.java)
                    .equalTo("errorType", logLevel.name)
                    .findAll()
                    .deleteAllFromRealm()

            // Save the new logs in the database
            it.copyToRealm(logs)
        }
    }

    fun saveRootDirs(rootDirs: List<RootDir>) {
        this.clearRootDirs()

        this.getRealm()?.executeTransaction {
            it.copyToRealmOrUpdate(rootDirs)
        }
    }

    fun saveSchedules(section: String, schedules: List<Schedule>) {
        this.getRealm()?.executeTransaction {
            schedules.forEach {
                this.prepareScheduleForSaving(it, section)
            }

            it.copyToRealmOrUpdate(schedules)
        }
    }

    fun saveSerie(serie: Serie) {
        this.getRealm()?.executeTransaction {
            it.copyToRealmOrUpdate(serie)
        }
    }

    fun saveShow(show: Show) {
        this.getRealm()?.executeTransaction {
            this.prepareShowForSaving(show)

            it.copyToRealmOrUpdate(show)
        }
    }

    fun saveShows(shows: List<Show>) {
        this.getRealm()?.executeTransaction {
            shows.forEach {
                this.prepareShowForSaving(it)
            }

            it.copyToRealmOrUpdate(shows)
        }

        // Remove information about shows that might have been removed
        val savedShows = this.getShows(null, null) ?: return
        val removedIndexerIds = savedShows.map(Show::indexerId) - shows.map(Show::indexerId)

        removedIndexerIds.forEach {
            // deleteShow has its own transaction, so we need to run this outside of the above transaction
            this.deleteShow(it)
        }
    }

    fun saveShowsStat(stat: ShowsStat) {
        this.getRealm()?.executeTransaction {
            it.delete(ShowsStat::class.java)
            it.copyToRealm(stat)
        }
    }

    fun saveShowStat(stat: ShowStat, indexerId: Int): RealmShowStat {
        val realmStat = this.getRealmStat(stat, indexerId)

        this.getRealm()?.executeTransaction {
            it.copyToRealmOrUpdate(realmStat)
        }

        return realmStat
    }

    fun saveShowWidget(showWidget: ShowWidget) {
        this.getRealm()?.executeTransaction {
            it.copyToRealmOrUpdate(showWidget)
        }
    }

    private fun clearRootDirs() {
        this.getRealm()?.executeTransaction {
            it.delete(RootDir::class.java)
        }
    }

    internal fun getRealm(): Realm? {
        return if (this.realm?.isClosed ?: true) {
            null
        } else {
            this.realm
        }
    }

    private fun getRealmStat(stat: ShowStat, indexerId: Int): RealmShowStat {
        return RealmShowStat().apply {
            this.downloaded = stat.getTotalDone()
            this.episodesCount = stat.total
            this.indexerId = indexerId
            this.snatched = stat.getTotalPending()
        }
    }

    private fun prepareEpisodeForSaving(episode: Episode, indexerId: Int, season: Int, episodeNumber: Int) {
        val id = Episode.buildId(indexerId, season, episodeNumber)
        val savedEpisode = this.getEpisode(id, null)

        episode.description = if (episode.description.isNullOrEmpty()) savedEpisode?.description else episode.description
        episode.fileSizeHuman = if (episode.fileSizeHuman.isNullOrEmpty()) savedEpisode?.fileSizeHuman else episode.fileSizeHuman
        episode.id = id
        episode.indexerId = indexerId
        episode.number = episodeNumber
        episode.season = season
    }

    private fun prepareEpisodeForSaving(episode: OmDbEpisode) {
        episode.id = OmDbEpisode.buildId(episode.seriesId ?: "", episode.season ?: "", episode.episode ?: "")
    }

    private fun prepareHistoryForSaving(history: History) {
        history.id = "${history.date}_${history.status}_${history.indexerId}_${history.season}_${history.episode}"
    }

    private fun prepareScheduleForSaving(schedule: Schedule, section: String) {
        schedule.id = "${schedule.indexerId}_${schedule.season}_${schedule.episode}"
        schedule.section = section
    }

    private fun prepareShowForSaving(show: Show) {
        val savedShow = this.getShow(show.indexerId)

        show.airs = if (show.airs.isNullOrEmpty()) savedShow?.airs else show.airs
        show.genre = RealmList<RealmString>().apply {
            this.addAll((if (show.genre?.isEmpty() ?: true) savedShow?.genre else show.genre) ?: emptyList())
        }
        show.imdbId = if (show.imdbId.isNullOrEmpty()) savedShow?.imdbId else show.imdbId
        show.location = if (show.location.isNullOrEmpty()) savedShow?.location else show.location
        show.qualityDetails = Quality().apply {
            this.archive = RealmList<RealmString>().apply {
                this.addAll((if (show.qualityDetails?.archive?.isEmpty() ?: true) savedShow?.qualityDetails?.archive else show.qualityDetails?.archive) ?: emptyList())
            }
            this.indexerId = show.indexerId
            this.initial = RealmList<RealmString>().apply {
                this.addAll((if (show.qualityDetails?.initial?.isEmpty() ?: true) savedShow?.qualityDetails?.initial else show.qualityDetails?.initial) ?: emptyList())
            }
        }
        show.seasonList = RealmList<RealmString>().apply {
            this.addAll((if (show.seasonList?.isEmpty() ?: true) savedShow?.seasonList else show.seasonList) ?: emptyList())
        }
    }
}
