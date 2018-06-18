package com.mgaetan89.showsrage.extension

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.TestActivity
import org.assertj.android.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewGroupExtensionTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java)

    private lateinit var view: ViewGroup

    @Before
    fun before() {
        this.view = FrameLayout(this.activityRule.activity)
    }

    @Test
    fun inflateAttached() {
        val view = this.view.inflate(R.layout.drawer_header, true)

        assertThat(view).isInstanceOf(FrameLayout::class.java)
        assertThat(view as FrameLayout).hasChildCount(1)

        val child = view.getChildAt(0)

        assertThat(child).isInstanceOf(LinearLayout::class.java)

        with(child as LinearLayout) {
            assertThat(this).hasChildCount(2)
            assertThat(this.getChildAt(0)).hasId(R.id.app_logo)
            assertThat(this.getChildAt(1)).hasId(R.id.app_name)
        }
    }

    @Test
    fun inflateNotAttached() {
        val view = this.view.inflate(R.layout.drawer_header)

        assertThat(view).isInstanceOf(LinearLayout::class.java)

        with(view as LinearLayout) {
            assertThat(this).hasChildCount(2)
            assertThat(this.getChildAt(0)).hasId(R.id.app_logo)
            assertThat(this.getChildAt(1)).hasId(R.id.app_name)
        }
    }
}
