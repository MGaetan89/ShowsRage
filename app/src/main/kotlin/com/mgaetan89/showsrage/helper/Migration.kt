package com.mgaetan89.showsrage.helper

import com.mgaetan89.showsrage.model.History
import com.mgaetan89.showsrage.model.LogEntry
import io.realm.DynamicRealm
import io.realm.RealmMigration
import io.realm.RealmSchema

class Migration : RealmMigration {
    override fun migrate(realm: DynamicRealm?, oldVersion: Long, newVersion: Long) {
        val schema = realm?.schema ?: return
        var localOldVersion = oldVersion

        if (localOldVersion == 0L) {
            this.updateToV1(schema)

            localOldVersion++
        }

        if (localOldVersion == 1L) {
            this.updateToV2(schema)

            localOldVersion++
        }

        if (localOldVersion == 2L) {
            this.updateToV3(schema)

            localOldVersion++
        }

        if (localOldVersion == 3L) {
            this.updateToV4(schema)

            localOldVersion++
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is Migration
    }

    private fun updateToV1(schema: RealmSchema) {
        schema.get(LogEntry::class.java.simpleName)
                .addIndex("group")
    }

    private fun updateToV2(schema: RealmSchema) {
        schema.get(History::class.java.simpleName)
                .removePrimaryKey()
                .addField("id", String::class.java)
                .transform {
                    it.set("id", "${it.getString("date")}_${it.getString("status")}_${it.getInt("indexerId")}_${it.getInt("season")}_${it.getInt("episode")}")
                }
                .addPrimaryKey("id")
    }

    private fun updateToV3(schema: RealmSchema) {
        schema.get(History::class.java.simpleName)
                .transform {
                    it.set("id", "${it.getString("date")}_${it.getString("status")}_${it.getInt("indexerId")}_${it.getInt("season")}_${it.getInt("episode")}")
                }
    }

    private fun updateToV4(schema: RealmSchema) {
        schema.get(LogEntry::class.java.simpleName)
                .removePrimaryKey()
    }
}
