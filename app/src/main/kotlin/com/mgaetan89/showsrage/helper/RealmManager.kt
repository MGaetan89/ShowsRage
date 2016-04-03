package com.mgaetan89.showsrage.helper

import android.content.Context
import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.RootDir
import io.realm.*

object RealmManager {
    private lateinit var realm: Realm

    fun clearHistory() {
        this.realm.clear(History::class.java)
    }

    fun getHistory(listener: RealmChangeListener?): RealmResults<History> {
        val query = this.realm.where(History::class.java)

        if (listener == null) {
            return query.findAllSorted("date", Sort.DESCENDING)
        }

        val history = query.findAllSortedAsync("date", Sort.DESCENDING)
        history.addChangeListener(listener)

        return history
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

    fun saveHistory(histories: List<History>) {
        this.realm.executeTransaction {
            it.copyToRealmOrUpdate(histories)
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
}
