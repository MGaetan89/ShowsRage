package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.model.RootDir
import io.realm.RealmResults
import org.assertj.core.api.Assertions.assertThat
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmManager_GetRootDirsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
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
