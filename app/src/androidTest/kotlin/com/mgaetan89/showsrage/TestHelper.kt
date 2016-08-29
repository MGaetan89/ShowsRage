package com.mgaetan89.showsrage

import android.content.Context
import com.mgaetan89.showsrage.helper.Migration
import com.mgaetan89.showsrage.helper.RealmManager
import io.realm.Realm
import io.realm.RealmConfiguration

fun initRealm(context: Context, testContext: Context?) {
    val configuration = RealmConfiguration.Builder(context).let {
        if (testContext != null) {
            val testFile = "test.realm"

            it.assetFile(testContext, testFile)
            it.name(testFile)
        }

        it.schemaVersion(Constants.DATABASE_VERSION)
        it.migration(Migration())
        it.build()
    }

    Realm.setDefaultConfiguration(configuration)
    RealmManager.init()
}
