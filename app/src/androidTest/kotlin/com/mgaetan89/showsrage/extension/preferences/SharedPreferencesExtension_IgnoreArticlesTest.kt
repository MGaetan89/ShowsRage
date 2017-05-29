package com.mgaetan89.showsrage.extension.preferences

import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.TestActivity
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.ignoreArticles
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPreferencesExtension_IgnoreArticlesTest {
	@JvmField
	@Rule
	val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

	private lateinit var preference: SharedPreferences

	@Before
	fun before() {
		this.preference = InstrumentationRegistry.getTargetContext().getPreferences()
	}

	@Test
	fun ignoreArticles_False() {
		this.preference.edit().putBoolean(Fields.IGNORE_ARTICLES.field, false).apply()

		val ignoreArticles = this.preference.ignoreArticles()

		assertThat(ignoreArticles).isFalse()
	}

	@Test
	fun getEpisodeSort_Missing() {
		assertThat(this.preference.contains(Fields.IGNORE_ARTICLES.field)).isFalse()

		val ignoreArticles = this.preference.ignoreArticles()

		assertThat(ignoreArticles).isFalse()
	}

	@Test
	fun ignoreArticles_True() {
		this.preference.edit().putBoolean(Fields.IGNORE_ARTICLES.field, true).apply()

		val ignoreArticles = this.preference.ignoreArticles()

		assertThat(ignoreArticles).isTrue()
	}

	@After
	fun after() {
		this.preference.edit().clear().apply()
	}
}
