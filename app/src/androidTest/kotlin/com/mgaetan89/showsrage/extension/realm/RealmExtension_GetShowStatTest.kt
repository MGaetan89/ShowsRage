package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShowStat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowStatTest : RealmTest() {
    @Test
    fun getShowStat() {
        val showStat = this.realm.getShowStat(INDEXER_ID)

        assertThat(showStat).isNotNull()
        assertThat(showStat!!.downloaded).isEqualTo(82)
        assertThat(showStat.episodesCount).isEqualTo(120)
        assertThat(showStat.indexerId).isEqualTo(INDEXER_ID)
        assertThat(showStat.snatched).isEqualTo(21)
    }

    @Test
    fun getShowStat_unknown() {
        val showStat = this.realm.getShowStat(-1)

        assertThat(showStat).isNull()
    }

    companion object {
        private const val INDEXER_ID = 248835
    }
}
