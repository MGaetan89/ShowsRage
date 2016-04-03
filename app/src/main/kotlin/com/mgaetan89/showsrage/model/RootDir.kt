package com.mgaetan89.showsrage.model

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RootDir : RealmObject() {
    @SerializedName("default")
    open var defaultDir: Int = 0
    @PrimaryKey
    open var location: String = ""
    open var valid: Int = 0
}
