package com.mgaetan89.showsrage.network

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.model.Indexer
import com.mgaetan89.showsrage.model.RealmString
import com.squareup.okhttp.*
import io.realm.RealmList
import io.realm.RealmObject
import retrofit.RequestInterceptor
import retrofit.RestAdapter
import retrofit.client.OkClient
import retrofit.converter.GsonConverter
import java.io.IOException
import java.net.Proxy
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SickRageApi private constructor() : RequestInterceptor {
    private var apiKey = ""
    private var apiUrl = ""
    private var credentials: String? = null
    private var okHttpClient: OkHttpClient? = null
    private var path = ""
    var services: SickRageServices? = null
        private set
    val videosUrl: String
        get() = this.apiUrl + "videos"
    private var webRoot = ""

    fun getApiUrl() = buildFullApiUrl(this.apiUrl, this.path, this.apiKey)

    fun getBannerUrl(indexerId: Int, indexer: Indexer?): String {
        if (indexer == null) {
            return "%s?cmd=show.getbanner".format(this.getApiUrl())
        }

        return "%s?cmd=show.getbanner&%s=%d".format(this.getApiUrl(), indexer.paramName, indexerId)
    }

    fun getFanArtUrl(indexerId: Int, indexer: Indexer?): String {
        if (indexer == null) {
            return "%s?cmd=show.getfanart".format(this.getApiUrl())
        }

        return "%s?cmd=show.getfanart&%s=%d".format(this.getApiUrl(), indexer.paramName, indexerId)
    }

    fun getOkHttpClient(useSelfSignedCertificate: Boolean): OkHttpClient {
        if (this.okHttpClient == null) {
            this.okHttpClient = OkHttpClient()
        }

        this.setAuthenticator()
        this.trustAllCertificates(useSelfSignedCertificate)

        return this.okHttpClient!!
    }

    fun getPosterUrl(indexerId: Int, indexer: Indexer?): String {
        if (indexer == null) {
            return "%s?cmd=show.getposter".format(this.getApiUrl())
        }

        return "%s?cmd=show.getposter&%s=%d".format(this.getApiUrl(), indexer.paramName, indexerId)
    }

    fun init(preferences: SharedPreferences) {
        val useHttps = preferences.getBoolean("use_https", false)
        val address = preferences.getString("server_address", "")
        val portNumber = preferences.getString("server_port_number", "")
        val selfSignedCertificate = preferences.getBoolean("self_signed_certificate", false)

        this.apiKey = preferences.getString("api_key", "")
        this.apiUrl = buildApiUrl(useHttps, address, portNumber)
        this.credentials = getCredentials(
                preferences.getBoolean("basic_auth", false),
                preferences.getString("server_username", null),
                preferences.getString("server_password", null)
        )
        this.path = getApiPath(preferences.getString("server_path", ""))
        this.webRoot = getWebRoot(this.path)

        val builder = RestAdapter.Builder()
        builder.setClient(OkClient(this.getOkHttpClient(selfSignedCertificate)))
        builder.setConverter(gsonConverter)
        builder.setEndpoint(this.apiUrl)
        builder.setRequestInterceptor(this)
        builder.setLogLevel(Constants.NETWORK_LOG_LEVEL)

        this.services = builder.build().create(SickRageServices::class.java)
    }

    override fun intercept(request: RequestInterceptor.RequestFacade) {
        request.addEncodedPathParam("api_path", this.path)
        request.addPathParam("api_key", this.apiKey)
        request.addEncodedPathParam("web_root", this.webRoot)
    }

    private fun setAuthenticator() {
        this.okHttpClient!!.authenticator = object : Authenticator {
            @Throws(IOException::class)
            override fun authenticate(proxy: Proxy, response: Response): Request? {
                val credentials = this@SickRageApi.credentials

                if (!credentials.isNullOrEmpty()) {
                    return response.request().newBuilder().header("Authorization", credentials).build()
                }

                return null
            }

            @Throws(IOException::class)
            override fun authenticateProxy(proxy: Proxy, response: Response): Request? {
                return null
            }
        }
    }

    private fun trustAllCertificates(useSelfSignedCertificate: Boolean) {
        if (!useSelfSignedCertificate) {
            return
        }

        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }
        })

        var sslContext: SSLContext? = null
        try {
            sslContext = SSLContext.getInstance("SSL")
            sslContext!!.init(null, trustAllCerts, SecureRandom())
        } catch (exception: KeyManagementException) {
            exception.printStackTrace()
        } catch (exception: NoSuchAlgorithmException) {
            exception.printStackTrace()
        }

        if (sslContext != null) {
            this.okHttpClient!!.sslSocketFactory = sslContext.socketFactory
        }

        this.okHttpClient!!.setHostnameVerifier { hostname, session -> true }
    }

    companion object {
        val gson by lazy {
            val realmStringListType = object : TypeToken<RealmList<RealmString>>() {}.type

            GsonBuilder()
                    .registerTypeAdapter(realmStringListType, object : TypeAdapter<RealmList<RealmString>>() {
                        override fun read(input: JsonReader?): RealmList<RealmString>? {
                            val list = RealmList<RealmString>()

                            if (input != null) {
                                input.beginArray()
                                while (input.hasNext()) {
                                    // Skip null values
                                    try {
                                        input.nextNull()
                                    } catch(exception: IllegalStateException) {
                                        Log.d("SickRageApi", "RealmList<RealmString> contains null values")
                                    }

                                    if (input.hasNext()) {
                                        val string = RealmString()
                                        string.value = input.nextString()

                                        list.add(string)
                                    }
                                }
                                input.endArray()
                            }

                            return list
                        }

                        override fun write(out: JsonWriter?, value: RealmList<RealmString>?) {
                        }

                    })
                    .setExclusionStrategies(object : ExclusionStrategy {
                        override fun shouldSkipField(f: FieldAttributes): Boolean {
                            return f.declaringClass.equals(RealmObject::class.java)
                        }

                        override fun shouldSkipClass(clazz: Class<*>): Boolean {
                            return false;
                        }
                    })
                    .create();
        }
        private val gsonConverter by lazy {
            GsonConverter(gson)
        }
        val instance by lazy { SickRageApi() }

        private fun buildApiUrl(useHttps: Boolean, address: String, portNumber: String): String {
            // Retrofit requires a non-empty endpoint
            // So we use the local url
            if (address.isNullOrEmpty()) {
                return "http://127.0.0.1/"
            }

            val builder = StringBuilder()
            builder.append(if (useHttps) "https://" else "http://")
            builder.append(address)

            if (portNumber.isNotEmpty()) {
                builder.append(":").append(portNumber)
            }

            builder.append("/")

            return builder.toString()
        }

        private fun buildFullApiUrl(apiUrl: String, path: String, apiKey: String): String {
            val builder = StringBuilder()
            builder.append(apiUrl)

            if (path.isNotEmpty()) {
                builder.append(path).append("/")
            }

            if (apiKey.isNotEmpty()) {
                builder.append(apiKey).append("/")
            }

            return builder.toString()
        }

        fun getApiPath(apiPath: String?): String {
            if (apiPath.isNullOrEmpty()) {
                return ""
            }

            return apiPath!!.replace("^/+|/$+".toRegex(), "")
        }

        fun getCredentials(useBasicAuthentication: Boolean, username: String?, password: String?): String? {
            if (!useBasicAuthentication) {
                return null
            }

            if (username.isNullOrEmpty()) {
                return null
            }

            if (password.isNullOrEmpty()) {
                return null
            }

            return Credentials.basic(username, password)
        }

        fun getWebRoot(apiPath: String?): String {
            if (apiPath.isNullOrEmpty()) {
                return ""
            }

            var path: String = apiPath!!

            // Add a leading /
            if (!path.startsWith("/")) {
                path = "/%s".format(path)
            }

            // Add a trailing /
            if (!path.endsWith("/")) {
                path += "/"
            }

            // If the path ends with /api/, we ignore that last segment
            if (path.endsWith("/api/")) {
                return path.substring(1, path.length - 4)
            }

            return apiPath.replace("^/+|/$+".toRegex(), "") + "/"
        }

        fun setCredentials(credentials: String?) {
            SickRageApi.instance.credentials = credentials
        }
    }
}
