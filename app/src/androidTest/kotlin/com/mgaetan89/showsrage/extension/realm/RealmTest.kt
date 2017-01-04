package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.rule.ActivityTestRule
import com.mgaetan89.showsrage.TestActivity
import io.realm.Realm
import io.realm.setContext
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule

abstract class RealmTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun configureRealm() {
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
