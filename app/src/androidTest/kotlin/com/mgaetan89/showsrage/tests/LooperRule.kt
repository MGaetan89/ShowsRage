package com.mgaetan89.showsrage.tests

import android.os.Looper
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class LooperRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                if (Looper.myLooper() == null) {
                    Looper.prepare()
                }

                base?.evaluate()

                Looper.myLooper().quit()
            }
        }
    }
}
