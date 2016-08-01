package com.mgaetan89.showsrage.helper

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.okhttp.OkHttpGlideModule
import com.bumptech.glide.integration.okhttp.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useSelfSignedCertificate
import com.mgaetan89.showsrage.network.SickRageApi
import java.io.InputStream

class OkHttpGlideModule : OkHttpGlideModule() {
    override fun registerComponents(context: Context?, glide: Glide?) {
        val selfSignedCertificate = context?.getPreferences()?.useSelfSignedCertificate() ?: false
        val client = SickRageApi.instance.getOkHttpClient(selfSignedCertificate)

        glide?.register(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory(client))
    }
}
