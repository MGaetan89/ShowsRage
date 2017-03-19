package com.mgaetan89.showsrage.extension.realm

import android.content.Intent
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.util.Log
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.activity.MainActivity
import com.mgaetan89.showsrage.helper.Utils
import io.realm.Realm
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule

abstract class RealmTest {
    @JvmField
    @Rule
    val activityRule = object : ActivityTestRule<MainActivity>(MainActivity::class.java) {
        override fun getActivityIntent(): Intent {
            return super.getActivityIntent().apply {
                this.putExtra(Constants.Bundle.INIT_ONLY, true)
            }
        }
    }

    val realm: Realm by lazy { Realm.getDefaultInstance() }

    val realmConfiguration = Utils.createRealmConfiguration("test.realm")

    @Before
    fun configureRealm() {
        Log.w("RealmTest", "Activity assets: ${this.activityRule.activity.assets.list("")}")
        Log.w("RealmTest", "Activity assets /: ${this.activityRule.activity.assets.list("/")}")
        Log.w("RealmTest", "Activity assets /assets: ${this.activityRule.activity.assets.list("/assets")}")
        Log.w("RealmTest", "Context assets: ${InstrumentationRegistry.getContext().assets.list("")}")
        Log.w("RealmTest", "Context assets /: ${InstrumentationRegistry.getContext().assets.list("/")}")
        Log.w("RealmTest", "Context assets /assets: ${InstrumentationRegistry.getContext().assets.list("/assets")}")
        Log.w("RealmTest", "Target Context assets: ${InstrumentationRegistry.getTargetContext().assets.list("")}")
        Log.w("RealmTest", "Target Context assets /: ${InstrumentationRegistry.getTargetContext().assets.list("/")}")
        Log.w("RealmTest", "Target Context assets /assets: ${InstrumentationRegistry.getTargetContext().assets.list("/assets")}")

        Realm.setDefaultConfiguration(this.realmConfiguration)

        this.realm.isAutoRefresh = false
    }

    @After
    fun after() {
        this.realm.close()

        Realm.deleteRealm(this.realmConfiguration)
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Looper.myLooper().quit()
        }
    }
}
