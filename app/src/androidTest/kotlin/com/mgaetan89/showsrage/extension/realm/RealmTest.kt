package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.helper.Migration
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.setContext
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule

abstract class RealmTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, true)

    val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun configureRealm() {
        setContext(null)

        Realm.init(this.activityRule.activity)

        setContext(InstrumentationRegistry.getContext())

        val configuration = RealmConfiguration.Builder().let {
            it.assetFile("test.realm")
            it.schemaVersion(Constants.DATABASE_VERSION)
            it.migration(Migration())
            it.build()
        }

        Realm.deleteRealm(configuration)
        Realm.setDefaultConfiguration(configuration)

        this.realm.isAutoRefresh = false
    }

    @After
    fun after() {
        this.realm.close()

        setContext(null)
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
