package com.mgaetan89.showsrage.helper

import android.content.Context
import com.mgaetan89.showsrage.model.*
import io.realm.*

object RealmManager {
    private lateinit var realm: Realm

    fun clearHistory() {
        this.realm.executeTransaction {
            it.delete(History::class.java)
        }
    }

    fun clearSchedule() {
        this.realm.executeTransaction {
            it.delete(Schedule::class.java)
        }
    }

    fun close() {
        if (!this.realm.isClosed) {
            this.realm.close()
        }
    }

    fun deleteShow(indexerId: Int) {
        this.realm.executeTransaction {
            // Remove the episodes associated to that show
            it.where(Episode::class.java).equalTo("indexerId", indexerId).findAll().deleteAllFromRealm()

            // Remove the quality associated to that show
            it.where(Quality::class.java).equalTo("indexerId", indexerId).findFirst().deleteFromRealm()

            // Remove the stat associated to that show
            it.where(RealmShowStat::class.java).equalTo("indexerId", indexerId).findFirst().deleteFromRealm()

            // Remove the show from the Show table
            it.where(Show::class.java).equalTo("indexerId", indexerId).findFirst().deleteFromRealm()
        }
    }

    fun getEpisode(episodeId: String, listener: RealmChangeListener<Episode>?): Episode? {
        val query = this.realm.where(Episode::class.java)
                .equalTo("id", episodeId)

        if (listener == null) {
            return query.findFirst()
        }

        val episode = query.findFirstAsync()
        episode.addChangeListener(listener)

        return episode
    }

    fun getEpisodes(indexerId: Int, season: Int, reversedOrder: Boolean, listener: RealmChangeListener<RealmResults<Episode>>): RealmResults<Episode> {
        val episodes = this.realm.where(Episode::class.java)
                .equalTo("indexerId", indexerId)
                .equalTo("season", season)
                .findAllSortedAsync("number", if (reversedOrder) Sort.DESCENDING else Sort.ASCENDING)
        episodes.addChangeListener(listener)

        return episodes
    }

    fun getHistory(listener: RealmChangeListener<RealmResults<History>>): RealmResults<History> {
        val history = this.realm.where(History::class.java).findAllSortedAsync("date", Sort.DESCENDING)
        history.addChangeListener(listener)

        return history
    }

    fun getLogs(logLevel: LogLevel, listener: RealmChangeListener<RealmResults<LogEntry>>): RealmResults<LogEntry> {
        val logLevels = this.getLogLevels(logLevel)
        val query = this.realm.where(LogEntry::class.java)

        if (logLevels.size == 1) {
            query.equalTo("errorType", logLevels.first())
        } else if (logLevels.size > 1) {
            query.beginGroup()
            logLevels.forEach {
                query.equalTo("errorType", it).or()
            }
            query.endGroup()
        }

        val logs = query.findAllSortedAsync("dateTime", Sort.DESCENDING)
        logs.addChangeListener(listener)

        return logs
    }

    fun getRootDirs(): RealmResults<RootDir> {
        return this.realm.where(RootDir::class.java).findAll()
    }

    fun getSchedule(section: String, listener: RealmChangeListener<RealmResults<Schedule>>): RealmResults<Schedule> {
        val schedule = this.realm.where(Schedule::class.java)
                .equalTo("section", section)
                .findAllSortedAsync("airDate")
        schedule.addChangeListener(listener)

        return schedule
    }

    fun getScheduleSections(): List<String> {
        return this.realm.where(Schedule::class.java).findAll().where().distinct("section").map { it.section }
    }

    fun getSerie(imdbId: String, listener: RealmChangeListener<Serie>): Serie {
        val serie = this.realm.where(Serie::class.java).equalTo("imdbId", imdbId).findFirstAsync()
        serie.addChangeListener(listener)

        return serie
    }

    fun getShow(indexerId: Int, listener: RealmChangeListener<Show>? = null): Show? {
        val query = this.realm.where(Show::class.java).equalTo("indexerId", indexerId)

        if (listener == null) {
            return query.findFirst()
        }

        val show = query.findFirstAsync()
        show.addChangeListener(listener)

        return show
    }

    fun getShows(anime: Boolean?, listener: RealmChangeListener<RealmResults<Show>>?): RealmResults<Show> {
        val query = this.realm.where(Show::class.java)

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

    fun getShowStat(indexerId: Int): RealmShowStat? {
        return this.realm.where(RealmShowStat::class.java)
                .equalTo("indexerId", indexerId)
                .findFirst()
    }

    fun init(context: Context) {
        val configuration = RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(configuration)

        this.realm = Realm.getDefaultInstance()
    }

    fun saveEpisode(episode: Episode, indexerId: Int, season: Int, episodeNumber: Int) {
        this.realm.executeTransaction {
            this.prepareEpisodeForSaving(episode, indexerId, season, episodeNumber)

            it.copyToRealmOrUpdate(episode)
        }
    }

    fun saveEpisodes(episodes: List<Episode>, indexerId: Int, season: Int) {
        this.realm.executeTransaction {
            episodes.forEach {
                this.prepareEpisodeForSaving(it, indexerId, season, it.number)
            }

            it.copyToRealmOrUpdate(episodes)
        }
    }

    fun saveHistory(histories: List<History>) {
        this.clearHistory()

        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(histories)
        }
    }

    fun saveLogs(logs: List<LogEntry>) {
        this.realm.executeTransaction {
            it.delete(LogEntry::class.java)
            it.copyToRealmOrUpdate(logs)
        }
    }

    fun saveRootDirs(rootDirs: List<RootDir>) {
        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(rootDirs)
        }
    }

    fun saveSchedules(section: String, schedules: List<Schedule>) {
        this.realm.executeTransaction {
            schedules.forEach {
                this.prepareScheduleForSaving(it, section)
            }

            it.copyToRealmOrUpdate(schedules)
        }
    }

    fun saveSerie(serie: Serie) {
        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(serie)
        }
    }

    fun saveShow(show: Show) {
        this.realm.executeTransaction {
            this.prepareShowForSaving(show)

            it.copyToRealmOrUpdate(show)
        }
    }

    fun saveShows(shows: List<Show>) {
        this.realm.executeTransaction {
            // Save the new shows data
            shows.forEach {
                this.prepareShowForSaving(it)
            }

            it.copyToRealmOrUpdate(shows)
        }

        // Remove information about shows that might have been removed
        val removedIndexerIds = this.getShows(null, null).map { it.indexerId } - shows.map { it.indexerId }

        removedIndexerIds.forEach {
            // deleteShow has its own transaction, so we need to run this outside of the above transaction
            this.deleteShow(it)
        }
    }

    fun saveShowStat(stat: ShowStat, indexerId: Int): RealmShowStat {
        val realmStat = this.getRealmStat(stat, indexerId)

        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(realmStat)
        }

        return realmStat
    }

    private fun getLogLevels(logLevel: LogLevel): Array<String> {
        return when (logLevel) {
            LogLevel.DEBUG -> arrayOf("DEBUG", "ERROR", "INFO", "WARNING")
            LogLevel.ERROR -> arrayOf("ERROR")
            LogLevel.INFO -> arrayOf("ERROR", "INFO", "WARNING")
            LogLevel.WARNING -> arrayOf("ERROR", "WARNING")
            else -> arrayOf("DEBUG", "ERROR", "INFO", "WARNING")
        }
    }

    private fun getRealmStat(stat: ShowStat, indexerId: Int): RealmShowStat {
        val realmStat = RealmShowStat()
        realmStat.downloaded = stat.getTotalDone()
        realmStat.episodesCount = stat.total
        realmStat.indexerId = indexerId
        realmStat.snatched = stat.getTotalPending()

        return realmStat
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

    private fun prepareScheduleForSaving(schedule: Schedule, section: String) {
        schedule.id = "${schedule.indexerId}_${schedule.season}_${schedule.episode}"
        schedule.section = section
    }

    private fun prepareShowForSaving(show: Show) {
        val savedShow = this.getShow(show.indexerId)

        show.airs = if (show.airs.isNullOrEmpty()) savedShow?.airs else show.airs
        show.genre = if (show.genre?.isEmpty() ?: true) savedShow?.genre else show.genre
        show.imdbId = if (show.imdbId.isNullOrEmpty()) savedShow?.imdbId else show.imdbId
        show.location = if (show.location.isNullOrEmpty()) savedShow?.location else show.location
        show.qualityDetails?.indexerId = show.indexerId
        show.seasonList = if (show.seasonList?.isEmpty() ?: true) savedShow?.seasonList else show.seasonList
    }
}
