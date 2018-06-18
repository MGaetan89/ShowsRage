package com.mgaetan89.showsrage.extension

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.widget.LinearLayout
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.TestActivity
import org.assertj.android.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContextExtensionTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

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
