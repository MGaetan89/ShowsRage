package com.mgaetan89.showsrage.extension.realm

import android.content.Intent
import android.os.Looper
import android.support.test.rule.ActivityTestRule
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
    private val realmConfiguration by lazy { Utils.createRealmConfiguration("test.realm") }

    @Before
    fun configureRealm() {
        this.activityRule.runOnUiThread {
            Realm.setDefaultConfiguration(this.realmConfiguration)
        }

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
