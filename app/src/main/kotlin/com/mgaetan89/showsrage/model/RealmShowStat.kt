package com.mgaetan89.showsrage.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmShowStat : RealmObject() {
	open var downloaded: Int = 0
	open var episodesCount: Int = 0
	@PrimaryKey
	open var indexerId: Int = 0
	open var snatched: Int = 0
}
