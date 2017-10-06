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

		if (localOldVersion == 4L) {
			// Database was updated to v5 during the development of v1.6
			// But no changes were kept in the final release of v1.6
			// So no work needs to be done here

			localOldVersion++
		}

		if (localOldVersion == 5L) {
			this.updateToV6(schema)

			localOldVersion++
		}

		if (localOldVersion == 6L) {
			this.updateToV7(schema)

			localOldVersion++
		}
	}

	override fun equals(other: Any?) = other is Migration

	private fun updateToV1(schema: RealmSchema) {
		schema.get(LogEntry::class.java.simpleName)
				?.addIndex("group")
	}

	private fun updateToV2(schema: RealmSchema) {
		schema.get(History::class.java.simpleName)
				?.removePrimaryKey()
				?.addField("id", String::class.java)
				?.transform {
					it.set("id", "${it.getString("date")}_${it.getString("status")}_${it.getInt("indexerId")}_${it.getInt("season")}_${it.getInt("episode")}")
				}
				?.addPrimaryKey("id")
	}

	private fun updateToV3(schema: RealmSchema) {
		schema.get(History::class.java.simpleName)
				?.transform {
					it.set("id", "${it.getString("date")}_${it.getString("status")}_${it.getInt("indexerId")}_${it.getInt("season")}_${it.getInt("episode")}")
				}
	}

	private fun updateToV4(schema: RealmSchema) {
		schema.get(LogEntry::class.java.simpleName)
				?.removePrimaryKey()
	}

	private fun updateToV6(schema: RealmSchema) {
		schema.remove("OmDbEpisode")
		schema.remove("Serie")
	}

	private fun updateToV7(schema: RealmSchema) {
		schema.get("Episode")
				?.setRequired("airDate", true)
				?.setRequired("id", true)
				?.setRequired("name", true)
				?.setRequired("quality", true)
				?.setRequired("subtitles", true)

		schema.get("History")?.setRequired("id", true)

		schema.get("LogEntry")
				?.setRequired("dateTime", true)
				?.setRequired("message", true)

		schema.get("RealmString")?.setRequired("value", true)

		schema.get("RootDir")?.setRequired("location", true)

		schema.get("Schedule")
				?.setRequired("airDate", true)
				?.setRequired("airs", true)
				?.setRequired("episodeName", true)
				?.setRequired("episodePlot", true)
				?.setRequired("id", true)
				?.setRequired("network", true)
				?.setRequired("quality", true)
				?.setRequired("section", true)
				?.setRequired("showName", true)
				?.setRequired("showStatus", true)

		schema.get("Show")
				?.setRequired("network", true)
				?.setRequired("nextEpisodeAirDate", true)
				?.setRequired("quality", true)
				?.setRequired("tvRageName", true)
	}
}
