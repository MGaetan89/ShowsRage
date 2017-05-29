package com.mgaetan89.showsrage.model

import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogEntryTest(
		val log: String?, val dateTime: String, val errorType: String,
		val errorColor: Int, val group: String?, val message: String
) {
	private lateinit var logEntry: LogEntry

	@Before
	fun before() {
		this.logEntry = LogEntry(this.log)
	}

	@Test
	fun getDateTime() {
		assertThat(this.logEntry.dateTime).isEqualTo(this.dateTime)
	}

	@Test
	fun getErrorColor() {
		assertThat(this.logEntry.getErrorColor()).isEqualTo(this.errorColor)
	}

	@Test
	fun getErrorType() {
		assertThat(this.logEntry.errorType).isEqualTo(this.errorType)
	}

	@Test
	fun getGroup() {
		assertThat(this.logEntry.group).isEqualTo(this.group)
	}

	@Test
	fun getMessage() {
		assertThat(this.logEntry.message).isEqualTo(this.message)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			return listOf(
					getInvalidLogEntry(null),
					getInvalidLogEntry(""),
					getInvalidLogEntry("2015-06-01 DEBUG Some DEBUG message"),
					getInvalidLogEntry("2015-06-01 DEBUG DAILYSEARCHER :: Some DEBUG message"),
					getValidLogEntry("17:28:45", "DEBUG", R.color.green, "Some DEBUG message"),
					getValidLogEntry("17:28:45", "DEBUG", R.color.green, "DAILYSEARCHER", "Some DEBUG message"),
					getValidLogEntry("2015-06-02 17:28:46", "DEBUG", R.color.green, "Some DEBUG message"),
					getValidLogEntry("2015-06-02 17:28:46", "DEBUG", R.color.green, "DAILYSEARCHER", "Some DEBUG message"),
					getInvalidLogEntry("2015-06-03 ERROR Some ERROR message"),
					getInvalidLogEntry("2015-06-03 ERROR DAILYSEARCHER :: Some ERROR message"),
					getValidLogEntry("17:28:47", "ERROR", R.color.red, "Some ERROR message"),
					getValidLogEntry("17:28:47", "ERROR", R.color.red, "DAILYSEARCHER", "Some ERROR message"),
					getValidLogEntry("2015-06-04 17:28:48", "ERROR", R.color.red, "Some ERROR message"),
					getValidLogEntry("2015-06-04 17:28:48", "ERROR", R.color.red, "DAILYSEARCHER", "Some ERROR message"),
					getInvalidLogEntry("2015-06-05 INFO Some INFO message"),
					getInvalidLogEntry("2015-06-05 INFO DAILYSEARCHER :: Some INFO message"),
					getValidLogEntry("17:28:49", "INFO", R.color.blue, "Some INFO message"),
					getValidLogEntry("17:28:49", "INFO", R.color.blue, "DAILYSEARCHER", "Some INFO message"),
					getValidLogEntry("2015-06-06 17:28:50", "INFO", R.color.blue, "Some INFO message"),
					getValidLogEntry("2015-06-06 17:28:50", "INFO", R.color.blue, "DAILYSEARCHER", "Some INFO message"),
					getInvalidLogEntry("2015-06-07 WARNING Some WARNING message"),
					getInvalidLogEntry("2015-06-07 WARNING DAILYSEARCHER :: Some WARNING message"),
					getValidLogEntry("17:28:51", "WARNING", R.color.orange, "Some WARNING message"),
					getValidLogEntry("17:28:51", "WARNING", R.color.orange, "DAILYSEARCHER", "Some WARNING message"),
					getValidLogEntry("2015-06-08 17:28:52", "WARNING", R.color.orange, "Some WARNING message"),
					getValidLogEntry("2015-06-08 17:28:52", "WARNING", R.color.orange, "DAILYSEARCHER", "Some WARNING message")
			)
		}

		private fun getInvalidLogEntry(log: String?): Array<Any?> {
			return arrayOf(log, "", "", android.R.color.black, "", "")
		}

		private fun getValidLogEntry(dateTime: String, errorType: String, errorColor: Int, message: String): Array<Any?> {
			return arrayOf(String.format("%s %s %s", dateTime, errorType, message), dateTime, errorType, errorColor, null, message)
		}

		private fun getValidLogEntry(dateTime: String, errorType: String, errorColor: Int, group: String, message: String): Array<Any?> {
			return arrayOf(String.format("%s %s %s :: %s", dateTime, errorType, group, message), dateTime, errorType, errorColor, group, message)
		}
	}
}
