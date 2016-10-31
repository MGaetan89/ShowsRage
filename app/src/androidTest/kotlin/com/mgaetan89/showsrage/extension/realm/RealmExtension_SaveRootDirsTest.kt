package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.saveRootDirs
import com.mgaetan89.showsrage.model.RootDir
import io.realm.Realm
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveRootDirsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        this.realm.isAutoRefresh = false

        assertThat(this.getRootDirs()).hasSize(1)
    }

    @Test
    fun saveRootDirs() {
        val rootDirs = mutableListOf<RootDir>()

        for (i in 1..3) {
            rootDirs.add(RootDir().apply {
                this.defaultDir = i % 2
                this.location = "/path/$i"
                this.valid = i % 2
            })
        }

        this.realm.saveRootDirs(rootDirs)

        assertThat(this.getRootDirs()).hasSize(3)
    }

    @Test
    fun saveRootDirs_empty() {
        this.realm.saveRootDirs(emptyList())

        assertThat(this.getRootDirs()).hasSize(0)
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getRootDirs() = this.realm.where(RootDir::class.java).findAll()

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
