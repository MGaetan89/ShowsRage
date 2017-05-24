package com.mgaetan89.showsrage.extension.realm

import android.support.test.runner.AndroidJUnit4
import com.mgaetan89.showsrage.extension.saveLogs
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.LogLevel
import io.realm.RealmResults
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@Ignore
@RunWith(AndroidJUnit4::class)
class RealmExtension_SaveLogsTest : RealmTest() {
    @Before
    fun before() {
        this.validateLogs()
    }

    @Test
    fun saveLogs() {
        val logs = (1..15).map {
            LogEntry().apply {
                this.dateTime = "dateTime_$it"
                this.errorType = LogLevel.DEBUG.name
                this.group = "group_$it"
                this.message = "message_$it"
            }
        }

        this.realm.saveLogs(LogLevel.DEBUG, logs)

        this.validateLogs(debugLogs = 15)
    }

    @Test
    fun saveLogs_newContent() {
        val logs = (1..15).map {
            LogEntry().apply {
                this.dateTime = "dateTime_$it"
                this.errorType = LogLevel.ERROR.name
                this.group = "group_$it"
                this.message = "message_$it"
            }
        }

        this.realm.saveLogs(LogLevel.ERROR, logs)

        this.validateLogs(errorLogs = 15)
    }

    @Test
    fun saveLogs_emptyHadContent() {
        this.realm.saveLogs(LogLevel.DEBUG, emptyList())

        this.validateLogs(debugLogs = 0)
    }

    @Test
    fun saveLogs_emptyNoContent() {
        this.realm.saveLogs(LogLevel.ERROR, emptyList())

        this.validateLogs()
    }

    @Test
    fun saveLogs_invalidEntries() {
        val logs = (1..15).map {
            LogEntry().apply {
                this.dateTime = if (it % 2 == 0) "dateTime_$it" else ""
                this.errorType = if (it % 2 == 0) LogLevel.DEBUG.name else ""
                this.group = if (it % 2 == 0) "group_$it" else ""
                this.message = if (it % 2 == 0) "message_$it" else ""
            }
        }

        this.realm.saveLogs(LogLevel.DEBUG, logs)

        this.validateLogs(debugLogs = 7)
    }

    private fun getLogs(logLevel: LogLevel? = null): RealmResults<LogEntry> {
        return if (logLevel == null) {
            this.realm.where(LogEntry::class.java).findAll()
        } else {
            this.realm.where(LogEntry::class.java).equalTo("errorType", logLevel.name).findAll()
        }
    }

    private fun validateLogs(debugLogs: Int = 50, errorLogs: Int = 0, infoLogs: Int = 50, warningLog: Int = 0) {
        assertThat(this.getLogs()).hasSize(debugLogs + errorLogs + infoLogs + warningLog)
        assertThat(this.getLogs(LogLevel.DEBUG)).hasSize(debugLogs)
        assertThat(this.getLogs(LogLevel.ERROR)).hasSize(errorLogs)
        assertThat(this.getLogs(LogLevel.INFO)).hasSize(infoLogs)
        assertThat(this.getLogs(LogLevel.WARNING)).hasSize(warningLog)
    }
}
