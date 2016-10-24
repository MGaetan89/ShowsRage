package com.mgaetan89.showsrage.extension

import android.support.annotation.Size
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

        // Remove the show from the Schedule table
        it.where(Schedule::class.java)
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

fun Realm.deleteShowWidgets(@Size(min = 1) widgetIds: Array<Int>) {
    this.executeTransaction {
        it.where(ShowWidget::class.java)
                .`in`("widgetId", widgetIds)
                .findAll()
                .deleteAllFromRealm()
    }
}

fun Realm.getEpisode(episodeId: String, listener: RealmChangeListener<Episode>): Episode {
    val episode = this.where(Episode::class.java)
            .equalTo("id", episodeId)
            .findFirstAsync()
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

fun Realm.getEpisodes(indexerId: Int, season: Int, reversedOrder: Boolean, listener: RealmChangeListener<RealmResults<Episode>>): RealmResults<Episode> {
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

fun Realm.getLogs(logLevel: LogLevel, groups: Array<String>?, listener: RealmChangeListener<RealmResults<LogEntry>>): RealmResults<LogEntry> {
    val logLevels = logLevel.logLevels
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
            .map(LogEntry::group)
            .filterNotNull()
            .sorted()
}

fun Realm.getRootDirs(): RealmResults<RootDir> {
    return this.where(RootDir::class.java)
            .findAll()
}

fun Realm.getSchedule(section: String, listener: RealmChangeListener<RealmResults<Schedule>>): RealmResults<Schedule> {
    val schedule = this.where(Schedule::class.java)
            .equalTo("section", section)
            .findAllSortedAsync("airDate")
    schedule.addChangeListener(listener)

    return schedule
}

fun Realm.getScheduleSections(): List<String> {
    return this.where(Schedule::class.java)
            .distinct("section")
            .map(Schedule::section)
            .filterNotNull()
}

fun Realm.getSeries(imdbId: String, listener: RealmChangeListener<RealmResults<Serie>>): RealmResults<Serie> {
    val series = this.where(Serie::class.java)
            .equalTo("imdbId", imdbId)
            .findAllAsync()
    series.addChangeListener(listener)

    return series
}

fun Realm.getShow(indexerId: Int): Show? {
    return this.where(Show::class.java)
            .equalTo("indexerId", indexerId)
            .findFirst()
}

fun Realm.getShow(indexerId: Int, listener: RealmChangeListener<Show>): Show {
    val show = this.where(Show::class.java)
            .equalTo("indexerId", indexerId)
            .findFirstAsync()
    show.addChangeListener(listener)

    return show
}

fun Realm.getShows(anime: Boolean?): RealmResults<Show>? {
    val query = this.where(Show::class.java)

    if (anime != null) {
        query.equalTo("anime", if (anime) 1 else 0)
    }

    return query.findAll()
}

fun Realm.getShows(anime: Boolean?, listener: RealmChangeListener<RealmResults<Show>>): RealmResults<Show> {
    val query = this.where(Show::class.java)

    if (anime != null) {
        query.equalTo("anime", if (anime) 1 else 0)
    }

    val shows = query.findAllAsync()
    shows.addChangeListener(listener)

    return shows
}

fun Realm.getShowsStats(listener: RealmChangeListener<RealmResults<ShowsStat>>): RealmResults<ShowsStat> {
    // There might be no data yet. So we request all data to be sure to always received a valid object.
    val stats = this.where(ShowsStat::class.java)
            .findAllAsync()
    stats.addChangeListener(listener)

    return stats
}

fun Realm.getShowStat(indexerId: Int): RealmShowStat? {
    val stat = this.where(RealmShowStat::class.java)
            .equalTo("indexerId", indexerId)
            .findFirst() ?: return null

    return this.copyFromRealm(stat)
}

fun Realm.getShowWidget(widgetId: Int): ShowWidget? {
    return this.where(ShowWidget::class.java)
            .equalTo("widgetId", widgetId)
            .findFirst()
}

fun Realm.saveEpisode(episode: Episode, indexerId: Int, season: Int, episodeNumber: Int) {
    this.executeTransaction {
        it.prepareEpisodeForSaving(episode, indexerId, season, episodeNumber)

        it.copyToRealmOrUpdate(episode)
    }
}

fun Realm.saveEpisode(episode: OmDbEpisode) {
    this.executeTransaction {
        prepareEpisodeForSaving(episode)

        it.copyToRealmOrUpdate(episode)
    }
}

fun Realm.saveEpisodes(episodes: List<Episode>, indexerId: Int, season: Int) {
    this.executeTransaction {
        episodes.forEach { episode ->
            it.prepareEpisodeForSaving(episode, indexerId, season, episode.number)
        }

        it.copyToRealmOrUpdate(episodes)
    }
}

fun Realm.saveHistory(histories: List<History>) {
    this.clearHistory()

    this.executeTransaction {
        histories.forEach(::prepareHistoryForSaving)

        it.copyToRealmOrUpdate(histories)
    }
}

fun Realm.saveLogs(logLevel: LogLevel, logs: List<LogEntry>) {
    this.executeTransaction {
        // Remove all existing logs for the level we are about to save
        it.where(LogEntry::class.java)
                .equalTo("errorType", logLevel.name)
                .findAll()
                .deleteAllFromRealm()

        // Remove invalid LogEntry
        val filteredLogs = logs.filter { !it.errorType.isNullOrEmpty() }

        // Save the new logs in the database
        it.copyToRealm(filteredLogs)
    }
}

fun Realm.saveRootDirs(rootDirs: List<RootDir>) {
    this.executeTransaction {
        it.delete(RootDir::class.java)
        it.copyToRealmOrUpdate(rootDirs)
    }
}

fun Realm.saveSchedules(section: String, schedules: List<Schedule>) {
    this.executeTransaction {
        schedules.forEach {
            prepareScheduleForSaving(it, section)
        }

        it.copyToRealmOrUpdate(schedules)
    }
}

fun Realm.saveSerie(serie: Serie) {
    this.executeTransaction {
        it.copyToRealmOrUpdate(serie)
    }
}

fun Realm.saveShow(show: Show) {
    this.executeTransaction {
        it.prepareShowForSaving(show)

        it.copyToRealmOrUpdate(show)
    }
}

fun Realm.saveShows(shows: List<Show>) {
    shows.forEach {
        this.saveShow(it)
    }

    // Remove information about shows that might have been removed
    val savedShows = this.getShows(null) ?: return
    val removedIndexerIds = savedShows.map(Show::indexerId) - shows.map(Show::indexerId)

    removedIndexerIds.forEach {
        this.deleteShow(it)
    }
}

fun Realm.saveShowsStat(stat: ShowsStat) {
    this.executeTransaction {
        it.delete(ShowsStat::class.java)
        it.copyToRealm(stat)
    }
}

fun Realm.saveShowStat(stat: ShowStat, indexerId: Int): RealmShowStat {
    val realmStat = getRealmShowStat(stat, indexerId)

    this.executeTransaction {
        it.copyToRealmOrUpdate(realmStat)
    }

    return realmStat
}

fun Realm.saveShowWidget(showWidget: ShowWidget) {
    this.executeTransaction {
        it.copyToRealmOrUpdate(showWidget)
    }
}

private fun Realm.getEpisode(episodeId: String): Episode? {
    return this.where(Episode::class.java)
            .equalTo("id", episodeId)
            .findFirst()
}

private fun Realm.prepareEpisodeForSaving(episode: Episode, indexerId: Int, season: Int, episodeNumber: Int) {
    val id = Episode.buildId(indexerId, season, episodeNumber)
    val savedEpisode = this.getEpisode(id)

    episode.description = if (episode.description.isNullOrEmpty()) savedEpisode?.description else episode.description
    episode.fileSizeHuman = if (episode.fileSizeHuman.isNullOrEmpty()) savedEpisode?.fileSizeHuman else episode.fileSizeHuman
    episode.id = id
    episode.indexerId = indexerId
    episode.number = episodeNumber
    episode.season = season
}

private fun Realm.prepareShowForSaving(show: Show) {
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

private fun getRealmShowStat(stat: ShowStat, indexerId: Int): RealmShowStat {
    return RealmShowStat().apply {
        this.downloaded = stat.getTotalDone()
        this.episodesCount = stat.total
        this.indexerId = indexerId
        this.snatched = stat.getTotalPending()
    }
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
