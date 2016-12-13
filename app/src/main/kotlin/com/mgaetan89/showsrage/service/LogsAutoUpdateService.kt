package com.mgaetan89.showsrage.service

import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import com.mgaetan89.showsrage.extension.getLogLevel
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.saveLogs
import com.mgaetan89.showsrage.model.LogEntry
import com.mgaetan89.showsrage.model.Logs
import com.mgaetan89.showsrage.network.SickRageApi
import io.realm.Realm
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class LogsAutoUpdateService : JobService() {
    private enum class JobStatus {
        FAILURE,
        RUNNING,
        SUCCESS,
        WAITING
    }

    private var status = JobStatus.WAITING

    override fun onStartJob(job: JobParameters?): Boolean {
        if (this.status == JobStatus.WAITING) {
            this.status = JobStatus.RUNNING

            val logLevel = this.getPreferences().getLogLevel()

            SickRageApi.instance.init(this.getPreferences())
            SickRageApi.instance.services?.getLogs(logLevel, object : Callback<Logs> {
                override fun failure(error: RetrofitError?) {
                    status = JobStatus.FAILURE
                }

                override fun success(logs: Logs?, response: Response?) {
                    status = JobStatus.SUCCESS

                    val logEntries = logs?.data?.map(::LogEntry) ?: emptyList()

                    Realm.getDefaultInstance().let {
                        it.saveLogs(logLevel, logEntries)
                        it.close()
                    }
                }
            })
        }

        return this.status == JobStatus.SUCCESS
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        return this.status == JobStatus.FAILURE
    }
}
