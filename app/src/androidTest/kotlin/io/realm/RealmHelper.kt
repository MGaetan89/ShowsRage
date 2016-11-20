package io.realm

import android.content.Context

fun setContext(context: Context?) {
    BaseRealm.applicationContext = context
}
