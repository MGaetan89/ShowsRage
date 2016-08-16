package com.mgaetan89.showsrage.extension

import com.mgaetan89.showsrage.model.Episode
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.LogLevel
import com.mgaetan89.showsrage.model.OmDbEpisode
import com.mgaetan89.showsrage.model.Quality
import com.mgaetan89.showsrage.model.RealmShowStat
import com.mgaetan89.showsrage.model.RootDir
import com.mgaetan89.showsrage.model.Schedule
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowWidget
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.Sort

fun Realm.clearHistory() {
    this.executeTransaction {
        it.delete(History::class.java)
    }
}

fun Realm.clearSchedule() {
    this.executeTransaction {
        it.delete(Schedule::class.java)
    }
}

fun Realm.deleteShow(indexerId: Int) {
    this.executeTransaction {
        // Remove the episodes associated to that show
        it.where(Episode::class.java)
                .equalTo("indexerId", indexerId)
                .findAll()
                .deleteAllFromRealm()

        // Remove the quality associated to that show
        it.where(Quality::class.java)
                .equalTo("indexerId", indexerId)
                .findAll()
                .deleteAllFromRealm()

        // Remove the stat associated to that show
        it.where(RealmShowStat::class.java)
                .equalTo("indexerId", indexerId)
                .findAll()
                .deleteAllFromRealm()

        // Remove the show from the Show table
        it.where(Show::class.java)
                .equalTo("indexerId", indexerId)
                .findAll()
                .deleteAllFromRealm()

        // Remove the show from the ShowWidget table
        it.where(ShowWidget::class.java)
                .equalTo("show.indexerId", indexerId)
                .findAll()
                .deleteAllFromRealm()
    }
}

fun Realm.getEpisode(episodeId: String, listener: RealmChangeListener<Episode>?): Episode? {
    val query = this.where(Episode::class.java)
            .equalTo("id", episodeId)

    if (listener == null) {
        return query.findFirst()
    }

    val episode = query.findFirstAsync()
    episode.addChangeListener(listener)

    return episode
}

fun Realm.getEpisodes(episodeId: String, listener: RealmChangeListener<RealmResults<OmDbEpisode>>): RealmResults<OmDbEpisode> {
    val episodes = this.where(OmDbEpisode::class.java)
            .equalTo("id", episodeId)
            .findAllAsync()
    episodes.addChangeListener(listener)

    return episodes
}

fun Realm.getEpisodes(indexerId: Int, season: Int, reversedOrder: Boolean, listener: RealmChangeListener<RealmResults<Episode>>?): RealmResults<Episode> {
    val episodes = this.where(Episode::class.java)
            .equalTo("indexerId", indexerId)
            .equalTo("season", season)
            .findAllSortedAsync("number", if (reversedOrder) Sort.DESCENDING else Sort.ASCENDING)
    episodes.addChangeListener(listener)

    return episodes
}

fun Realm.getHistory(listener: RealmChangeListener<RealmResults<History>>): RealmResults<History> {
    val history = this.where(History::class.java)
            .findAllSortedAsync("date", Sort.DESCENDING)
    history.addChangeListener(listener)

    return history
}

fun Realm.getLogs(logLevel: LogLevel, listener: RealmChangeListener<RealmResults<LogEntry>>): RealmResults<LogEntry> {
    return this.getLogs(logLevel, null, listener)
}

fun Realm.getLogs(logLevel: LogLevel, groups: Array<String>?, listener: RealmChangeListener<RealmResults<LogEntry>>): RealmResults<LogEntry> {
    val logLevels = getLogLevels(logLevel)
    val query = this.where(LogEntry::class.java)

    if (groups != null && groups.isNotEmpty()) {
        query.`in`("group", groups)
    }

    if (logLevels.isNotEmpty()) {
        query.`in`("errorType", logLevels)
    }

    val logs = query.findAllSortedAsync("dateTime", Sort.DESCENDING)
    logs.addChangeListener(listener)

    return logs
}

fun Realm.getLogsGroup(): List<String> {
    return this.where(LogEntry::class.java)
            .distinct("group")
            .map { it.group }
            .filterNotNull()
            .sorted()
}

fun Realm.getRootDirs(): RealmResults<RootDir> {
    return this.where(RootDir::class.java)
            .findAll()
}

private fun getLogLevels(logLevel: LogLevel): Array<String> {
    return when (logLevel) {
        LogLevel.DEBUG -> arrayOf("DEBUG", "ERROR", "INFO", "WARNING")
        LogLevel.ERROR -> arrayOf("ERROR")
        LogLevel.INFO -> arrayOf("ERROR", "INFO", "WARNING")
        LogLevel.WARNING -> arrayOf("ERROR", "WARNING")
    }
}
