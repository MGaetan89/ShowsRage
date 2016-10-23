package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getServerAddress
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetServerAddressTest {
    @JvmField
    @Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
    }

    @Test
    fun getServerAddress() {
        this.preference.edit().putString(Fields.SERVER_ADDRESS.field, "serverAddress").apply()

        val serverAddress = this.preference.getServerAddress()

        assertThat(serverAddress).isEqualTo("serverAddress")
    }

    @Test
    fun getServerAddress_Empty() {
        this.preference.edit().putString(Fields.SERVER_ADDRESS.field, "").apply()

        val serverAddress = this.preference.getServerAddress()

        assertThat(serverAddress).isEmpty()
    }

    @Test
    fun getServerAddress_Missing() {
        assertThat(this.preference.contains(Fields.SERVER_ADDRESS.field)).isFalse()

        val serverAddress = this.preference.getServerAddress()

        assertThat(serverAddress).isEmpty()
    }

    @Test
    fun getServerAddress_Null() {
        this.preference.edit().putString(Fields.SERVER_ADDRESS.field, null).apply()

        val serverAddress = this.preference.getServerAddress()

        assertThat(serverAddress).isEmpty()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
