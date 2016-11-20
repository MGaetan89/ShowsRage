package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.deleteShowWidgets
import com.mgaetan89.showsrage.model.ShowWidget
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_DeleteShowWidgetsTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getShowWidgets()).hasSize(20)
    }

    @Test
    fun deleteShowWidgets() {
        this.realm.deleteShowWidgets(arrayOf(68))

        assertThat(this.getShowWidgets()).hasSize(19)
    }

    @Test
    fun deleteShowWidgets_multiple() {
        this.realm.deleteShowWidgets(arrayOf(68, 101))

        assertThat(this.getShowWidgets()).hasSize(18)
    }

    @Test
    fun deleteShowWidgets_unknown() {
        this.realm.deleteShowWidgets(arrayOf(-1))

        assertThat(this.getShowWidgets()).hasSize(20)
    }

    private fun getShowWidgets() = this.realm.where(ShowWidget::class.java).findAll()
}
