package com.mgaetan89.showsrage.extension

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
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
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(this.activityRule.activity, InstrumentationRegistry.getContext())

        assertThat(this.getShowWidgets()).hasSize(0) // TODO Include some Show Widgets in the test database
    }

    @Test
    fun deleteShowWidgets() {
        this.realm.deleteShowWidgets(arrayOf(INDEXER_ID))

        assertThat(this.getShowWidgets()).isEmpty()
    }

    @After
    fun after() {
        this.realm.close()
    }

    private fun getShowWidgets() = this.realm.where(ShowWidget::class.java).findAll()

    companion object {
        private const val INDEXER_ID = 121361

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
