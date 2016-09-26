package com.mgaetan89.showsrage

import android.content.Context
import com.mgaetan89.showsrage.helper.Migration
import io.realm.Realm
import io.realm.RealmConfiguration

private const val REALM_FILENAME = "test.realm"

fun initRealm(context: Context, testContext: Context) {
    val configuration = RealmConfiguration.Builder(context).let {
        it.assetFile(testContext, REALM_FILENAME)
        it.name(REALM_FILENAME)
        it.schemaVersion(Constants.DATABASE_VERSION)
        it.migration(Migration())
        it.build()
    }

    Realm.setDefaultConfiguration(configuration)
}
