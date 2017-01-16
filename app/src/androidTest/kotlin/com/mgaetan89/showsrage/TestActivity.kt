package com.mgaetan89.showsrage

import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.v7.app.AppCompatActivity
import com.mgaetan89.showsrage.helper.Migration
import io.realm.Realm
import io.realm.RealmConfiguration

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(InstrumentationRegistry.getContext())

        val configuration = RealmConfiguration.Builder()
                .assetFile("test.realm")
                .directory(this.applicationContext.filesDir)
                .schemaVersion(Constants.DATABASE_VERSION)
                .migration(Migration())
                .build()

        Realm.deleteRealm(configuration)
        Realm.setDefaultConfiguration(configuration)
    }
}
