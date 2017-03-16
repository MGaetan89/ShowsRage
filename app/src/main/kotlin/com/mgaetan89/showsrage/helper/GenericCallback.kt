package com.mgaetan89.showsrage.helper

import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.mgaetan89.showsrage.model.GenericResponse
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response
import java.lang.ref.WeakReference

open class GenericCallback(activity: FragmentActivity) : Callback<GenericResponse> {
    val activityReference = WeakReference(activity)

    override fun failure(error: RetrofitError?) {
        error?.printStackTrace()
    }

    override fun success(genericResponse: GenericResponse?, response: Response?) {
        if (genericResponse?.message?.isNotBlank() ?: false) {
            val activity = this.getActivity() ?: return

            Toast.makeText(activity, genericResponse!!.message, Toast.LENGTH_SHORT).show()
        }
    }

    protected fun getActivity(): FragmentActivity? {
        return this.activityReference.get()
    }
}
