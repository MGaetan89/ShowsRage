package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getServerPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetServerPathTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun getServerPath() {
        this.preference.edit().putString(Fields.SERVER_PATH.field, "serverPath").apply()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEqualTo("serverPath")
    }

    @Test
    fun getServerPath_Empty() {
        this.preference.edit().putString(Fields.SERVER_PATH.field, "").apply()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEmpty()
    }

    @Test
    fun getServerPath_LeadingSlash() {
        this.preference.edit().putString(Fields.SERVER_PATH.field, "/server/path").apply()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEqualTo("server/path")
    }

    @Test
    fun getServerPath_Missing() {
        assertThat(this.preference.contains(Fields.SERVER_PATH.field)).isFalse()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEmpty()
    }

    @Test
    fun getServerPath_Null() {
        this.preference.edit().putString(Fields.SERVER_PATH.field, null).apply()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEmpty()
    }

    @Test
    fun getServerPath_TrailingSlash() {
        this.preference.edit().putString(Fields.SERVER_PATH.field, "server/path/").apply()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEqualTo("server/path")
    }

    @Test
    fun getServerPath_Slash() {
        this.preference.edit().putString(Fields.SERVER_PATH.field, "/server/path/").apply()

        val serverPath = this.preference.getServerPath()

        assertThat(serverPath).isEqualTo("server/path")
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
