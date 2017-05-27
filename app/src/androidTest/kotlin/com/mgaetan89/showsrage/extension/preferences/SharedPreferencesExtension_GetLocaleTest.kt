package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.getPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_GetLocaleTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

	private lateinit var preference: SharedPreferences

	@Before
	fun before() {
		this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
	}

	@Test
	fun getLocale_English_En() {
		Locale.setDefault(Locale.ENGLISH)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "en").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@Test
	fun getLocale_English_Fr() {
		Locale.setDefault(Locale.ENGLISH)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "fr").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.FRENCH)
	}

	@Test
	fun getLocale_English_It() {
		Locale.setDefault(Locale.ENGLISH)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "it").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@Test
	fun getLocale_French_En() {
		Locale.setDefault(Locale.FRENCH)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "en").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@Test
	fun getLocale_French_Fr() {
		Locale.setDefault(Locale.FRENCH)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "fr").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.FRENCH)
	}

	@Test
	fun getLocale_French_It() {
		Locale.setDefault(Locale.FRENCH)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "it").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.FRENCH)
	}

	@Test
	fun getLocale_Missing_SupportedLocale_English() {
		Locale.setDefault(Locale.ENGLISH)

		assertThat(this.preference.contains(Fields.DISPLAY_LANGUAGE.field)).isFalse()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@Test
	fun getLocale_Missing_SupportedLocale_French() {
		Locale.setDefault(Locale.FRENCH)

		assertThat(this.preference.contains(Fields.DISPLAY_LANGUAGE.field)).isFalse()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.FRENCH)
	}

	@Test
	fun getLocale_Missing_UnsupportedLocale() {
		Locale.setDefault(Locale.GERMAN)

		assertThat(this.preference.contains(Fields.DISPLAY_LANGUAGE.field)).isFalse()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@Test
	fun getLocale_UnsupportedLocale_En() {
		Locale.setDefault(Locale.GERMAN)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "en").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@Test
	fun getLocale_UnsupportedLocale_Fr() {
		Locale.setDefault(Locale.GERMAN)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "fr").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.FRENCH)
	}

	@Test
	fun getLocale_UnsupportedLocale_It() {
		Locale.setDefault(Locale.GERMAN)

		this.preference.edit().putString(Fields.DISPLAY_LANGUAGE.field, "it").apply()

		val locale = this.preference.getLocale()

		assertThat(locale).isEqualTo(Locale.ENGLISH)
	}

	@After
	fun after() {
		this.preference.edit().clear().apply()
	}
}
