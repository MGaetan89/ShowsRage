package io.realm

import android.content.Context

fun clearContext() {
    BaseRealm.applicationContext = null
}

fun setContext(context: Context) {
    BaseRealm.applicationContext = context
}
