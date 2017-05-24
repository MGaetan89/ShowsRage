package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getScheduleSections
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmExtension_GetScheduleSectionsTest : RealmTest() {
    @Test
    fun getScheduleSections() {
        val scheduleSections = this.realm.getScheduleSections()

        assertThat(scheduleSections).hasSize(4)
        assertThat(scheduleSections).containsOnly("soon", "later", "today", "missed")
    }
}
