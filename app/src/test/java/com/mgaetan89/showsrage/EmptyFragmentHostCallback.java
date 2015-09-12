package com.mgaetan89.showsrage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentHostCallback;

public class EmptyFragmentHostCallback extends FragmentHostCallback {
	public EmptyFragmentHostCallback(Context context) {
		super(context, null, 0);
	}

	@Nullable
	@Override
	public Object onGetHost() {
		return null;
	}
}
