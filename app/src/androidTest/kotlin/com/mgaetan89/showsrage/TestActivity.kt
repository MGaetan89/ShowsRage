package com.mgaetan89.showsrage

import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.v7.app.AppCompatActivity
import com.mgaetan89.showsrage.helper.Utils

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utils.initRealm(InstrumentationRegistry.getTargetContext(), "test.realm", deleteRealm = true)
    }
}
