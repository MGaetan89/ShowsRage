package com.mgaetan89.showsrage.extension

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetPortNumberTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getPortNumber() {
        this.preference.edit().putString(Fields.PORT_NUMBER.field, "portNumber").apply()

        val portNumber = this.preference.getPortNumber()

        assertThat(portNumber).isEqualTo("portNumber")
    }

    @Test
    fun getPortNumber_Empty() {
        this.preference.edit().putString(Fields.PORT_NUMBER.field, "").apply()

        val portNumber = this.preference.getPortNumber()

        assertThat(portNumber).isEmpty()
    }

    @Test
    fun getPortNumber_Missing() {
        assertThat(this.preference.contains(Fields.PORT_NUMBER.field)).isFalse()

        val portNumber = this.preference.getPortNumber()

        assertThat(portNumber).isEmpty()
    }

    @Test
    fun getPortNumber_Null() {
        this.preference.edit().putString(Fields.PORT_NUMBER.field, null).apply()

        val portNumber = this.preference.getPortNumber()

        assertThat(portNumber).isEmpty()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
