package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.model.LogEntry
import io.realm.DynamicRealm
import io.realm.RealmMigration

class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema = realm?.schema ?: return
        var localOldVersion = oldVersion

        if (localOldVersion == 0L) {
            schema.get(LogEntry::class.java.simpleName)
                    .addIndex("group")

            localOldVersion++
        }
    }
}
