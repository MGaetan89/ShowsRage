package io.realm

import android.content.Context

fun setRealmApplicationContext(context: Context?) {
    BaseRealm.applicationContext = context
}
