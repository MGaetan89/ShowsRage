package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.saveShowWidget
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.Show
import com.mgaetan89.showsrage.model.ShowWidget
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
class RealmExtension_SaveShowWidgetTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())

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

    @After
    fun after() {
        this.realm.isAutoRefresh = false
        this.realm.close()
    }

    private fun getShowWidgets() = this.realm.where(ShowWidget::class.java).findAll()

    companion object {
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
