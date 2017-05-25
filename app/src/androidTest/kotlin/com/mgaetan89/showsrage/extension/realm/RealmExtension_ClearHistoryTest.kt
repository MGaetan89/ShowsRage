package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.clearHistory
import com.mgaetan89.showsrage.model.History
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_ClearHistoryTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getHistory()).hasSize(100)
    }

    @Test
    fun clearHistory() {
        this.realm.clearHistory()

        assertThat(this.getHistory()).isEmpty()
    }

    private fun getHistory() = this.realm.where(History::class.java).findAll()
}
