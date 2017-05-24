package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveRootDirs
import com.mgaetan89.showsrage.model.RootDir
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveRootDirsTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getRootDirs()).hasSize(1)
    }

    @Test
    fun saveRootDirs() {
        val rootDirs = (1..3).map {
            RootDir().apply {
                this.defaultDir = it % 2
                this.location = "/path/$it"
                this.valid = it % 2
            }
        }

        this.realm.saveRootDirs(rootDirs)

        assertThat(this.getRootDirs()).hasSize(3)
    }

    @Test
    fun saveRootDirs_empty() {
        this.realm.saveRootDirs(emptyList())

        assertThat(this.getRootDirs()).hasSize(0)
    }

    private fun getRootDirs() = this.realm.where(RootDir::class.java).findAll()
}
