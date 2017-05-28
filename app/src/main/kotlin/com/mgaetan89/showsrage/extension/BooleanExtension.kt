package com.mgaetan89.showsrage.extension

fun Boolean?.toInt() = if (this ?: false) 1 else 0
