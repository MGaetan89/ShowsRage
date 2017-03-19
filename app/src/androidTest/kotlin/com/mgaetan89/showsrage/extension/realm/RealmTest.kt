package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
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
    val activityRule = ActivityTestRule(MainActivity::class.java)

    val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun configureRealm() {
        Utils.initRealm(InstrumentationRegistry.getTargetContext(), "test.realm")

        this.realm.isAutoRefresh = false
    }

    @After
    fun after() {
        this.realm.close()

        val realmConfiguration = Utils.createRealmConfiguration("test.realm")

        Realm.deleteRealm(realmConfiguration)
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
