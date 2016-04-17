package com.mgaetan89.showsrage.helper

import android.content.Context
import com.mgaetan89.showsrage.model.*
import io.realm.*

object RealmManager {
    private lateinit var realm: Realm

    fun clearHistory() {
        this.realm.clear(History::class.java)
    }

    fun getEpisode(episodeId: String, listener: RealmChangeListener?): Episode? {
        val query = this.realm.where(Episode::class.java)
                .equalTo("id", episodeId)

        if (listener == null) {
            return query.findFirst()
        }

        val episode = query.findFirstAsync()
        episode.addChangeListener(listener)

        return episode
    }

    fun getEpisodes(indexerId: Int, season: Int, reversedOrder: Boolean, listener: RealmChangeListener): RealmResults<Episode> {
        val episodes = this.realm.where(Episode::class.java)
                .equalTo("indexerId", indexerId)
                .equalTo("season", season)
                .findAllSortedAsync("number", if (reversedOrder) Sort.DESCENDING else Sort.ASCENDING)
        episodes.addChangeListener(listener)

        return episodes
    }

    fun getHistory(listener: RealmChangeListener): RealmResults<History> {
        val history = this.realm.where(History::class.java).findAllSortedAsync("date", Sort.DESCENDING)
        history.addChangeListener(listener)

        return history
    }

    fun getLogs(logLevel: LogLevel, listener: RealmChangeListener): RealmResults<LogEntry> {
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
            for (episode in episodes) {
                this.prepareEpisodeForSaving(episode, indexerId, season, episode.number)
            }

            it.copyToRealmOrUpdate(episodes)
        }
    }

    fun saveHistory(histories: List<History>) {
        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(histories)
        }
    }

    fun saveLogs(logs: List<LogEntry>) {
        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(logs)
        }
    }

    fun saveRootDirs(rootDirs: List<RootDir>) {
        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(rootDirs)
        }
    }

    fun close() {
        if (!this.realm.isClosed) {
            this.realm.close()
        }
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
}
