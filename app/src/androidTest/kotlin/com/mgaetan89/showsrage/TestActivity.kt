package com.mgaetan89.showsrage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mgaetan89.showsrage.helper.Utils
import io.realm.Realm

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.initRealm(this, "test.realm")
    }

    override fun onDestroy() {
        val realmConfiguration = Utils.createRealmConfiguration("test.realm")

        Realm.deleteRealm(realmConfiguration)

        super.onDestroy()
    }
}
