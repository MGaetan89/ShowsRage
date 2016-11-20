package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveShowWidget
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowWidget
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveShowWidgetTest : RealmTest() {
    @Before
    fun before() {
        assertThat(this.getShowWidgets()).hasSize(20)
    }

    @Test
    fun saveShowWidget() {
        this.realm.saveShowWidget(ShowWidget().apply {
            this.widgetId = 1
            this.show = realm.where(Show::class.java).equalTo("indexerId", 79175).findFirst()
        })

        assertThat(this.getShowWidgets()).hasSize(21)
    }

    private fun getShowWidgets() = this.realm.where(ShowWidget::class.java).findAll()
}
