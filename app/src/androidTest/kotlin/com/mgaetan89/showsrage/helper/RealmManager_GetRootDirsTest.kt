package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.RootDir
import io.realm.RealmResults
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmManager_GetRootDirsTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init()
    }

    @Test
    fun getRootDirsSync() {
        val rootDirs = RealmManager.getRootDirs()

        this.validateRootDirs(rootDirs)
    }

    @After
    fun after() {
        RealmManager.close()
    }

    private fun validateRootDirs(rootDirs: RealmResults<RootDir>?) {
        assertThat(rootDirs).isNotNull()
        assertThat(rootDirs).hasSize(1)

        val rootDir = rootDirs!!.first()
        assertThat(rootDir).isNotNull()
        assertThat(rootDir.defaultDir).isEqualTo(1)
        assertThat(rootDir.location).isEqualTo("/volume1/Séries")
        assertThat(rootDir.valid).isEqualTo(1)
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
