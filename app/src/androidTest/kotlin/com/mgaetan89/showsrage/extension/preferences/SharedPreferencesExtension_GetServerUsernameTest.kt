package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.getServerUsername
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetServerUsernameTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

	private lateinit var preference: SharedPreferences

	@Before
	fun before() {
		this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
	}

	@Test
	fun getServerUsername() {
		this.preference.edit().putString(Fields.SERVER_USERNAME.field, "serverUsername").apply()

		val serverUsername = this.preference.getServerUsername()

		assertThat(serverUsername).isEqualTo("serverUsername")
	}

	@Test
	fun getServerUsername_Empty() {
		this.preference.edit().putString(Fields.SERVER_USERNAME.field, "").apply()

		val serverUsername = this.preference.getServerUsername()

		assertThat(serverUsername).isEmpty()
	}

	@Test
	fun getServerUsername_Missing() {
		assertThat(this.preference.contains(Fields.SERVER_USERNAME.field)).isFalse()

		val serverUsername = this.preference.getServerUsername()

		assertThat(serverUsername).isNull()
	}

	@Test
	fun getServerUsername_Null() {
		this.preference.edit().putString(Fields.SERVER_USERNAME.field, null).apply()

		val serverUsername = this.preference.getServerUsername()

		assertThat(serverUsername).isNull()
	}

	@After
	fun after() {
		this.preference.edit().clear().apply()
	}
}
