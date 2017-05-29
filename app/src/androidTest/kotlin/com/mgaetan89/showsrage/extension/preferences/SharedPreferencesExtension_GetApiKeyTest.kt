package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getApiKey
import com.mgaetan89.showsrage.extension.getPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetApiKeyTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

	private lateinit var preference: SharedPreferences

	@Before
	fun before() {
		this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
	}

	@Test
	fun getApiKey() {
		this.preference.edit().putString(Fields.API_KEY.field, "apiKey").apply()

		val apiKey = this.preference.getApiKey()

		assertThat(apiKey).isEqualTo("apiKey")
	}

	@Test
	fun getApiKey_Empty() {
		this.preference.edit().putString(Fields.API_KEY.field, "").apply()

		val apiKey = this.preference.getApiKey()

		assertThat(apiKey).isEmpty()
	}

	@Test
	fun getApiKey_Missing() {
		assertThat(this.preference.contains(Fields.API_KEY.field)).isFalse()

		val apiKey = this.preference.getApiKey()

		assertThat(apiKey).isEmpty()
	}

	@Test
	fun getApiKey_Null() {
		this.preference.edit().putString(Fields.API_KEY.field, null).apply()

		val apiKey = this.preference.getApiKey()

		assertThat(apiKey).isEmpty()
	}

	@After
	fun after() {
		this.preference.edit().clear().apply()
	}
}
