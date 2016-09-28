package com.mgaetan89.showsrage

import android.content.Context
import com.mgaetan89.showsrage.helper.Migration
import io.realm.Realm
import io.realm.RealmConfiguration

fun initRealm(context: Context, testContext: Context) {
    val configuration = RealmConfiguration.Builder(context).let {
        it.assetFile(testContext, "test.realm")
        it.schemaVersion(Constants.DATABASE_VERSION)
        it.migration(Migration())
        it.build()
    }

    Realm.deleteRealm(configuration)
    Realm.setDefaultConfiguration(configuration)
}
