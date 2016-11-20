package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShowWidget
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowWidgetTest : RealmTest() {
    @Test
    fun getShowWidget() {
        val showWidget = this.realm.getShowWidget(WIDGET_ID)

        assertThat(showWidget).isNotNull()
        assertThat(showWidget!!.show).isEqualTo(this.realm.getShow(248741))
        assertThat(showWidget.widgetId).isEqualTo(WIDGET_ID)
    }

    @Test
    fun getShowWidget_unknown() {
        val showWidget = this.realm.getShowWidget(-1)

        assertThat(showWidget).isNull()
    }

    companion object {
        private const val WIDGET_ID = 105
    }
}
