package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.deleteShowWidgets
import com.mgaetan89.showsrage.initRealm
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
class RealmExtension_DeleteShowWidgetsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())

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

    @After
    fun after() {
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
