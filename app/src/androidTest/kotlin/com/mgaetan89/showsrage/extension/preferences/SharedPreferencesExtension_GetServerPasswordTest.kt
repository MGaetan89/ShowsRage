package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getServerPassword
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetServerPasswordTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun getServerPassword() {
        this.preference.edit().putString(Fields.SERVER_PASSWORD.field, "serverPassword").apply()

        val serverPassword = this.preference.getServerPassword()

        assertThat(serverPassword).isEqualTo("serverPassword")
    }

    @Test
    fun getServerPassword_Empty() {
        this.preference.edit().putString(Fields.SERVER_PASSWORD.field, "").apply()

        val serverPassword = this.preference.getServerPassword()

        assertThat(serverPassword).isEmpty()
    }

    @Test
    fun getServerPassword_Missing() {
        assertThat(this.preference.contains(Fields.SERVER_PASSWORD.field)).isFalse()

        val serverPassword = this.preference.getServerPassword()

        assertThat(serverPassword).isNull()
    }

    @Test
    fun getServerPassword_Null() {
        this.preference.edit().putString(Fields.SERVER_PASSWORD.field, null).apply()

        val serverPassword = this.preference.getServerPassword()

        assertThat(serverPassword).isNull()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
