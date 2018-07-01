package com.mgaetan89.showsrage.extension

import android.os.Build
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.LinearLayout
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.TestActivity
import org.assertj.android.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class ContextExtensionTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    @Test
    fun getLocalizedTime_en() {
        val context = this.activityRule.activity
        context.resources.changeLocale(Locale.ENGLISH)

        assertThat(context.getLocalizedTime("", "")).isNull()
        assertThat(context.getLocalizedTime("", "yyyy-MM-dd")).isNull()
        assertThat(context.getLocalizedTime("02:34:56", "")).isNull()
        assertThat(context.getLocalizedTime("02:34:56", "yyyy-MM-dd")).isNull()
        assertThat(context.getLocalizedTime("30.06.2018", "")).isNull()
        assertThat(context.getLocalizedTime("30.06.2018", "yyyy-MM-dd")).isNull()
        assertThat(context.getLocalizedTime("2018-06-30", "")).isNull()
        assertThat(context.getLocalizedTime("2018-06-30 02:34:56", "")).isNull()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            assertThat(context.getLocalizedTime("2018-06-30", "yyyy-MM-dd")).isEqualTo("12:00 AM")
            assertThat(context.getLocalizedTime("2018-06-30 02:34:56", "yyyy-MM-dd")).isEqualTo("12:00 AM")
        }
    }

    @Test
    fun getLocalizedTime_fr() {
        val context = this.activityRule.activity
        context.resources.changeLocale(Locale.FRENCH)

        assertThat(context.getLocalizedTime("", "")).isNull()
        assertThat(context.getLocalizedTime("", "yyyy-MM-dd")).isNull()
        assertThat(context.getLocalizedTime("02:34:56", "")).isNull()
        assertThat(context.getLocalizedTime("02:34:56", "yyyy-MM-dd")).isNull()
        assertThat(context.getLocalizedTime("30.06.2018", "")).isNull()
        assertThat(context.getLocalizedTime("30.06.2018", "yyyy-MM-dd")).isNull()
        assertThat(context.getLocalizedTime("2018-06-30", "")).isNull()
        assertThat(context.getLocalizedTime("2018-06-30", "yyyy-MM-dd")).isEqualTo("00:00")
        assertThat(context.getLocalizedTime("2018-06-30 02:34:56", "")).isNull()
        assertThat(context.getLocalizedTime("2018-06-30 02:34:56", "yyyy-MM-dd")).isEqualTo("00:00")
    }

    @Test
    fun inflate() {
        val view = this.activityRule.activity.inflate(R.layout.drawer_header)

        assertThat(view).isInstanceOf(LinearLayout::class.java)

        with(view as LinearLayout) {
            assertThat(this).hasChildCount(2)
            assertThat(this.getChildAt(0)).hasId(R.id.app_logo)
            assertThat(this.getChildAt(1)).hasId(R.id.app_name)
        }
    }
}
