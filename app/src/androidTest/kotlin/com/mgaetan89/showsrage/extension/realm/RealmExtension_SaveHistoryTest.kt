package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveHistory
import com.mgaetan89.showsrage.model.History
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveHistoryTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getHistory()).hasSize(100)
    }

    @Test
    fun saveHistory() {
        val historiesToSave = mutableListOf<History>()

        for (i in 1..3) {
            historiesToSave.add(History().apply {
                this.episode = i
                this.date = "date_$i"
                this.indexerId = INDEXER_ID
                this.season = i * 10
                this.status = "status_$i"
            })
        }

        this.realm.saveHistory(historiesToSave)

        val histories = this.getHistory()

        assertThat(histories).hasSize(3)

        for (i in 1..3) {
            val history = histories[i - 1]

            assertThat(history.episode).isEqualTo(i)
            assertThat(history.date).isEqualTo("date_$i")
            assertThat(history.id).isEqualTo("date_${i}_status_${i}_${INDEXER_ID}_${i * 10}_$i")
            assertThat(history.indexerId).isEqualTo(INDEXER_ID)
            assertThat(history.season).isEqualTo(i * 10)
            assertThat(history.status).isEqualTo("status_$i")
        }
    }

    @Test
    fun saveHistory_empty() {
        this.realm.saveHistory(emptyList())

        assertThat(this.getHistory()).hasSize(0)
    }

    private fun getHistory() = this.realm.where(History::class.java).findAll()

    companion object {
        private const val INDEXER_ID = 73838
    }
}
