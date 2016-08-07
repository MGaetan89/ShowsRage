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
class SharedPreferencesExtension_UseSelfSignedCertificateTest {
    @JvmField
    @Rule
    val activityRule: ActivityTestRule<TestActivity> = ActivityTestRule(TestActivity::class.java)

    private lateinit var preference: SharedPreferences

    @Before
    fun before() {
        this.preference = this.activityRule.activity.getPreferences()
    }

    @Test
    fun useSelfSignedCertificate_False() {
        this.preference.edit().putBoolean(Fields.SELF_SIGNED_CERTIFICATE.field, false).apply()

        val useSelfSignedCertificate = this.preference.useSelfSignedCertificate()

        assertThat(useSelfSignedCertificate).isFalse()
    }

    @Test
    fun useSelfSignedCertificate_Missing() {
        this.preference.edit().putBoolean(Fields.SELF_SIGNED_CERTIFICATE.field, false).apply()

        val useSelfSignedCertificate = this.preference.useSelfSignedCertificate()

        assertThat(useSelfSignedCertificate).isFalse()
    }

    @Test
    fun useSelfSignedCertificate_Null() {
        val useSelfSignedCertificate = null.useSelfSignedCertificate()

        assertThat(useSelfSignedCertificate).isFalse()
    }

    @Test
    fun useSelfSignedCertificate_True() {
        this.preference.edit().putBoolean(Fields.SELF_SIGNED_CERTIFICATE.field, true).apply()

        val useSelfSignedCertificate = this.preference.useSelfSignedCertificate()

        assertThat(useSelfSignedCertificate).isTrue()
    }

    @After
    fun after() {
        this.preference.edit().clear().apply()
    }
}
