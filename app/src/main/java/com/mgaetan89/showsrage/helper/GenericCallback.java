package com.mgaetan89.showsrage.helper;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.mgaetan89.showsrage.model.GenericResponse;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GenericCallback implements Callback<GenericResponse> {
	private WeakReference<FragmentActivity> activityReference = null;

	public GenericCallback(FragmentActivity activity) {
		this.activityReference = new WeakReference<>(activity);
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		FragmentActivity activity = this.getActivity();

		if (activity != null) {
			Toast.makeText(activity, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Nullable
	protected final FragmentActivity getActivity() {
		return this.activityReference.get();
	}
}
