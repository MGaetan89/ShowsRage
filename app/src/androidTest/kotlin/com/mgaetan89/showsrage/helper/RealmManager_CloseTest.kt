package com.mgaetan89.showsrage.helper

import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions

@RunWith(AndroidJUnit4::class)
class RealmManager_CloseTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    @Before
    fun before() {
        RealmManager.init(this.activityRule.activity, InstrumentationRegistry.getContext())
    }

    @Test
    @Ignore
    fun close() {
        val realm = spy(RealmManager.getRealm())

        RealmManager.close()

        verify(realm)!!.close()
        assertThat(realm!!.isClosed).isTrue()
        assertThat(RealmManager.getRealm()).isNull()
    }

    @Test
    @Ignore
    fun closeTwice() {
        val realm = spy(RealmManager.getRealm())

        RealmManager.close()
        RealmManager.close()

        verifyNoMoreInteractions(realm)
    }

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
