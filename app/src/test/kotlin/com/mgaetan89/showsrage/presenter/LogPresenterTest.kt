package com.mgaetan89.showsrage.presenter

import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.model.LogEntry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class LogPresenterTest(val logEntry: LogEntry?, val dateTime: CharSequence?, val errorColor: Int, val errorType: String, val message: String) {
    private lateinit var presenter: LogPresenter

    @Before
    fun before() {
        this.presenter = LogPresenter(this.logEntry)
    }

    @Test
    fun getDateTime() {
        assertThat(this.presenter.getDateTime()).isEqualTo(this.dateTime)
    }

    @Test
    fun getErrorColor() {
        assertThat(this.presenter.getErrorColor()).isEqualTo(this.errorColor)
    }

    @Test
    fun getErrorType() {
        assertThat(this.presenter.getErrorType()).isEqualTo(this.errorType)
    }

    @Test
    fun getMessage() {
        assertThat(this.presenter.getMessage()).isEqualTo(this.message)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf(null, "", android.R.color.white, "", ""),
                    arrayOf(LogEntry("2015-12-31 01:02:03 DEBUG Some debug message"), null, R.color.green, "DEBUG", "Some debug message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 ERROR Some error message"), null, R.color.red, "ERROR", "Some error message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 INFO Some info message"), null, R.color.blue, "INFO", "Some info message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 WARNING Some warning message"), null, R.color.orange, "WARNING", "Some warning message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 DEBUG DAILYSEARCHER :: Some debug message"), null, R.color.green, "DEBUG", "DAILYSEARCHER :: Some debug message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 ERROR DAILYSEARCHER :: Some error message"), null, R.color.red, "ERROR", "DAILYSEARCHER :: Some error message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 INFO DAILYSEARCHER :: Some info message"), null, R.color.blue, "INFO", "DAILYSEARCHER :: Some info message"),
                    arrayOf(LogEntry("2015-12-31 01:02:03 WARNING DAILYSEARCHER :: Some warning message"), null, R.color.orange, "WARNING", "DAILYSEARCHER :: Some warning message")
            )
        }
    }
}
