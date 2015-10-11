package com.mgaetan89.showsrage.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.mgaetan89.showsrage.network.SickRageApi;
import com.squareup.okhttp.OkHttpClient;

import java.io.InputStream;

public class OkHttpGlideModule extends com.bumptech.glide.integration.okhttp.OkHttpGlideModule {
	@Override
	public void registerComponents(Context context, Glide glide) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean selfSignedCertificate = preferences.getBoolean("self_signed_certificate", false);
		OkHttpClient client = SickRageApi.getInstance().getOkHttpClient(selfSignedCertificate);

		glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
	}
}
