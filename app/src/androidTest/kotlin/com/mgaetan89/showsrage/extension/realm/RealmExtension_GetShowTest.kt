package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShow
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowTest : RealmTest() {
    @Test
    fun getShow() {
        val show = this.realm.getShow(INDEXER_ID)

        assertThat(show).isNotNull()
        assertThat(show!!.indexerId).isEqualTo(INDEXER_ID)
        assertThat(show.showName).isEqualTo("Futurama")
    }

    @Test
    fun getShow_unknown() {
        val show = this.realm.getShow(-1)

        assertThat(show).isNull()
    }

    companion object {
        private const val INDEXER_ID = 73871
    }
}
