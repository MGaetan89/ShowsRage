package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getShow
import com.mgaetan89.showsrage.extension.getShowWidget
import com.mgaetan89.showsrage.initRealm
import io.realm.Realm
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetShowWidgetTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())
    }

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

    @After
    fun after() {
        this.realm.isAutoRefresh = false
        this.realm.close()
    }

    companion object {
        private const val WIDGET_ID = 105

        @BeforeClass
        @JvmStatic
        fun beforeClass() {
            if (Looper.myLooper() == null) {
                Looper.prepare()
            }
        }

        @AfterClass
        @JvmStatic
        fun afterClass() {
            Looper.myLooper().quit()
        }
    }
}
