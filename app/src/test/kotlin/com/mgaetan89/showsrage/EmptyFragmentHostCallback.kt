package com.mgaetan89.showsrage

import android.content.Context
import android.support.v4.app.FragmentHostCallback

class EmptyFragmentHostCallback(context: Context) : FragmentHostCallback<Any>(context, null, 0) {
	override fun onGetHost() = null
}
