package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getRootDirs
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetRootDirsTest : RealmTest() {
    @Test
    fun getRootDirs() {
        val rootDirs = this.realm.getRootDirs()

        assertThat(rootDirs).hasSize(1)
        assertThat(rootDirs.first().location).isEqualTo("/volume1/Séries")
    }
}
