package com.mgaetan89.showsrage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mgaetan89.showsrage.helper.Migration
import io.realm.Realm
import io.realm.RealmConfiguration

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(this)

        val configuration = RealmConfiguration.Builder().let {
            it.assetFile("test.realm")
            it.schemaVersion(Constants.DATABASE_VERSION)
            it.migration(Migration())
            it.build()
        }

        Realm.deleteRealm(configuration)
        Realm.setDefaultConfiguration(configuration)
    }
}
