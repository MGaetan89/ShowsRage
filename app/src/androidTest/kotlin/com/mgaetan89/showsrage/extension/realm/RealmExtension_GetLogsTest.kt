package com.mgaetan89.showsrage.extension.realm

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.annotation.UiThreadTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.getLogs
import com.mgaetan89.showsrage.initRealm
import com.mgaetan89.showsrage.model.LogLevel
import io.realm.Realm
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetLogsTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private val realm: Realm by lazy { Realm.getDefaultInstance() }

    @Before
    fun before() {
        initRealm(InstrumentationRegistry.getTargetContext(), InstrumentationRegistry.getContext())
    }

    @Test
    @UiThreadTest
    fun getLogs_existingLogLevel_emptyGroups() {
        this.realm.getLogs(EXISTING_LOG_LEVEL, emptyArray<String>(), RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(50)

            it.forEach {
                assertThat(it.errorType).isEqualTo(EXISTING_LOG_LEVEL.name)
            }

            // TODO Check that the shows are sorted
            //assertThat(it).isSorted()
        })
    }

    @Test
    @UiThreadTest
    fun getLogs_existingLogLevel_notGroups() {
        this.realm.getLogs(EXISTING_LOG_LEVEL, null, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(50)

            it.forEach {
                assertThat(it.errorType).isEqualTo(EXISTING_LOG_LEVEL.name)
            }

            // TODO Check that the shows are sorted
            //assertThat(it).isSorted()
        })
    }

    @Test
    @UiThreadTest
    fun getLogs_existingLogLevel_withGroups() {
        this.realm.getLogs(EXISTING_LOG_LEVEL, arrayOf("WRONG GROUP", "POSTPROCESSER"), RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).hasSize(4)

            it.forEach {
                assertThat(it.errorType).isEqualTo(EXISTING_LOG_LEVEL.name)
            }

            // TODO Check that the shows are sorted
            //assertThat(it).isSorted()
        })
    }

    @Test
    @UiThreadTest
    fun getLogs_missingLogLevel_emptyGroups() {
        this.realm.getLogs(MISSING_LOG_LEVEL, emptyArray<String>(), RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).isEmpty()
        })
    }

    @Test
    @UiThreadTest
    fun getLogs_missingLogLevel_notGroups() {
        this.realm.getLogs(MISSING_LOG_LEVEL, null, RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).isEmpty()
        })
    }

    @Test
    @UiThreadTest
    fun getLogs_missingLogLevel_withGroups() {
        this.realm.getLogs(MISSING_LOG_LEVEL, arrayOf("WRONG GROUP", "POSTPROCESSER"), RealmChangeListener {
            it.removeChangeListeners()

            assertThat(it).isNotNull()
            assertThat(it).isEmpty()
        })
    }

    @After
    fun after() {
        this.realm.isAutoRefresh = false
        this.realm.close()
    }

    companion object {
        private val EXISTING_LOG_LEVEL = LogLevel.INFO
        private val MISSING_LOG_LEVEL = LogLevel.ERROR

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
