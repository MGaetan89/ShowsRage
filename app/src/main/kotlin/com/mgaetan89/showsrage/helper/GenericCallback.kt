package com.mgaetan89.showsrage.helper

import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.mgaetan89.showsrage.model.GenericResponse
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

class GenericCallback : Callback<GenericResponse> {
    private val activityReference: WeakReference<FragmentActivity>

    constructor(activity: FragmentActivity) {
        this.activityReference = WeakReference(activity)
    }

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun success(genericResponse: GenericResponse?, response: Response?) {
        val activity = this.getActivity()

        if (activity != null && genericResponse != null) {
            Toast.makeText(activity, genericResponse.message, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun getActivity(): FragmentActivity? {
        return this.activityReference.get()
    }
}
