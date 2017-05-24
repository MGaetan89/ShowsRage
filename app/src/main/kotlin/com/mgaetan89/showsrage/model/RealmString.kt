package com.mgaetan89.showsrage.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmString(@PrimaryKey open var value: String = "") : RealmObject()
