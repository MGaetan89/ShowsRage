package com.mgaetan89.showsrage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mgaetan89.showsrage.helper.Utils

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.initRealm(this, "test.realm", deleteRealm = true)
    }
}
