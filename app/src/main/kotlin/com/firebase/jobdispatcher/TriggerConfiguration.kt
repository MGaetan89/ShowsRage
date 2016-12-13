package com.firebase.jobdispatcher

// FIXME https://github.com/firebase/firebase-jobdispatcher-android/issues/5
class TriggerConfiguration {
    companion object {
        fun executionWindow(builder: Job.Builder, windowStart: Int, windowEnd: Int) {
            builder.trigger = Trigger.executionWindow(windowStart, windowEnd)
        }
    }
}
