package com.mgaetan89.showsrage.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Quality : RealmObject() {
	open var archive: RealmList<RealmString>? = null
	@PrimaryKey
	open var indexerId: Int = 0
	open var initial: RealmList<RealmString>? = null
}
