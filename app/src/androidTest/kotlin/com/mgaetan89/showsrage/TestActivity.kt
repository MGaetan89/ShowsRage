package com.mgaetan89.showsrage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mgaetan89.showsrage.helper.Utils
import io.realm.clearContext

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clearContext()

        Utils.initRealm(this, "test.realm", deleteRealm = true)
    }
}
