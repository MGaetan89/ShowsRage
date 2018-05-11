package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.getLogs
import com.mgaetan89.showsrage.model.LogLevel
import io.realm.RealmChangeListener
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RealmExtension_GetLogsTest : RealmTest() {
	@Before
	fun before() {
		this.realm.isAutoRefresh = true
	}

	@Test
	fun getLogs_existingLogLevel_emptyGroups() {
		this.realm.getLogs(EXISTING_LOG_LEVEL, emptyArray(), RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).hasSize(50)

			it.forEach {
				assertThat(it.errorType).isEqualTo(EXISTING_LOG_LEVEL.name)
			}

			for (i in 1 until it.size) {
				assertThat(it[i].dateTime < it[i - 1].dateTime).isTrue()
			}
		})
	}

	@Test
	fun getLogs_existingLogLevel_noGroups() {
		this.realm.getLogs(EXISTING_LOG_LEVEL, null, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).hasSize(50)

			it.forEach {
				assertThat(it.errorType).isEqualTo(EXISTING_LOG_LEVEL.name)
			}

			for (i in 1 until it.size) {
				assertThat(it[i].dateTime < it[i - 1].dateTime).isTrue()
			}
		})
	}

	@Test
	fun getLogs_existingLogLevel_withGroups() {
		this.realm.getLogs(EXISTING_LOG_LEVEL, arrayOf("WRONG GROUP", "POSTPROCESSER"), RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).hasSize(4)

			it.forEach {
				assertThat(it.errorType).isEqualTo(EXISTING_LOG_LEVEL.name)
			}

			for (i in 1 until it.size) {
				assertThat(it[i].dateTime < it[i - 1].dateTime).isTrue()
			}
		})
	}

	@Test
	fun getLogs_missingLogLevel_emptyGroups() {
		this.realm.getLogs(MISSING_LOG_LEVEL, emptyArray(), RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).isEmpty()
		})
	}

	@Test
	fun getLogs_missingLogLevel_noGroups() {
		this.realm.getLogs(MISSING_LOG_LEVEL, null, RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).isEmpty()
		})
	}

	@Test
	fun getLogs_missingLogLevel_withGroups() {
		this.realm.getLogs(MISSING_LOG_LEVEL, arrayOf("WRONG GROUP", "POSTPROCESSER"), RealmChangeListener {
			it.removeAllChangeListeners()

			assertThat(it).isNotNull()
			assertThat(it).isEmpty()
		})
	}

	companion object {
		private val EXISTING_LOG_LEVEL = LogLevel.INFO
		private val MISSING_LOG_LEVEL = LogLevel.ERROR
	}
}
