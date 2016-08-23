package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.model.History
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

        if (localOldVersion == 1L) {
            schema.get(History::class.java.simpleName)
                    .removePrimaryKey()
                    .addField("id", String::class.java)
                    .transform {
                        it.set("id", "${it.getString("date")}_${it.getString("status")}_${it.getInt("indexerId")}_${it.getInt("season")}_${it.getInt("episode")}")
                    }
                    .addPrimaryKey("id")

            localOldVersion++
        }

        if (localOldVersion == 2L) {
            schema.get(History::class.java.simpleName)
                    .transform {
                        it.set("id", "${it.getString("date")}_${it.getString("status")}_${it.getInt("indexerId")}_${it.getInt("season")}_${it.getInt("episode")}")
                    }

            localOldVersion++
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Migration
    }
}
