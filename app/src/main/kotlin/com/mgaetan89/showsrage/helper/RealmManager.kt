package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.model.RootDir
import io.realm.Realm
import io.realm.RealmResults

object RealmManager {
    fun getRootDirs(): RealmResults<RootDir> {
        var realm = Realm.getDefaultInstance()
        val rootDirs = realm.where(RootDir::class.java).findAll()
        realm.close()

        return rootDirs
    }

    fun saveRootDirs(rootDirs: List<RootDir>) {
        with(Realm.getDefaultInstance()) {
            executeTransaction {
                copyToRealmOrUpdate(rootDirs)
            }

            close()
        }
    }
}
