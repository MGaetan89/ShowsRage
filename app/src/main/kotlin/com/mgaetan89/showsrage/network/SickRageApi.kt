package com.mgaetan89.showsrage.network

import android.content.SharedPreferences
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mgaetan89.showsrage.BuildConfig
import com.mgaetan89.showsrage.extension.getApiKey
import com.mgaetan89.showsrage.extension.getPortNumber
import com.mgaetan89.showsrage.extension.getServerAddress
import com.mgaetan89.showsrage.extension.getServerPassword
import com.mgaetan89.showsrage.extension.getServerPath
import com.mgaetan89.showsrage.extension.getServerUsername
import com.mgaetan89.showsrage.extension.useBasicAuth
import com.mgaetan89.showsrage.extension.useHttps
import com.mgaetan89.showsrage.extension.useSelfSignedCertificate
import com.mgaetan89.showsrage.model.Indexer
import com.squareup.okhttp.Authenticator
import com.squareup.okhttp.Credentials
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
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
import java.util.concurrent.TimeUnit
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
		val okHttpClient = this.okHttpClient ?: OkHttpClient().apply {
			this.setConnectTimeout(30, TimeUnit.SECONDS)
			this.setReadTimeout(30, TimeUnit.SECONDS)
		}

		this.setAuthenticator()
		this.trustAllCertificates(useSelfSignedCertificate)

		this.okHttpClient = okHttpClient

		return okHttpClient
	}

	fun getPosterUrl(indexerId: Int, indexer: Indexer?): String {
		if (indexer == null) {
			return "%s?cmd=show.getposter".format(this.getApiUrl())
		}

		return "%s?cmd=show.getposter&%s=%d".format(this.getApiUrl(), indexer.paramName, indexerId)
	}

	fun init(preferences: SharedPreferences) {
		val useHttps = preferences.useHttps()
		val address = preferences.getServerAddress()
		val portNumber = preferences.getPortNumber()
		val selfSignedCertificate = preferences.useSelfSignedCertificate()
		val logLevel = if (BuildConfig.DEBUG) RestAdapter.LogLevel.FULL else RestAdapter.LogLevel.NONE

		this.apiKey = preferences.getApiKey()
		this.apiUrl = buildApiUrl(useHttps, address, portNumber)
		this.credentials = getCredentials(
				preferences.useBasicAuth(),
				preferences.getServerUsername(),
				preferences.getServerPassword()
		)
		this.path = preferences.getServerPath()
		this.webRoot = getWebRoot(this.path)

		val builder = RestAdapter.Builder()
		builder.setClient(OkClient(this.getOkHttpClient(selfSignedCertificate)))
		builder.setConverter(gsonConverter)
		builder.setEndpoint(this.apiUrl)
		builder.setRequestInterceptor(this)
		builder.setLogLevel(logLevel)

		this.services = builder.build().create(SickRageServices::class.java)
	}

	override fun intercept(request: RequestInterceptor.RequestFacade) {
		request.addEncodedPathParam("api_path", this.path)
		request.addPathParam("api_key", this.apiKey)
		request.addEncodedPathParam("web_root", this.webRoot)
	}

	private fun setAuthenticator() {
		this.okHttpClient?.authenticator = object : Authenticator {
			@Throws(IOException::class)
			override fun authenticate(proxy: Proxy, response: Response): Request? {
				val credentials = this@SickRageApi.credentials

				if (!credentials.isNullOrEmpty()) {
					return response.request().newBuilder().header("Authorization", credentials).build()
				}

				return null
			}

			@Throws(IOException::class)
			override fun authenticateProxy(proxy: Proxy, response: Response) = null
		}
	}

	private fun trustAllCertificates(useSelfSignedCertificate: Boolean) {
		if (!useSelfSignedCertificate) {
			return
		}

		val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
			@Throws(CertificateException::class)
			override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit

			@Throws(CertificateException::class)
			override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit

			override fun getAcceptedIssuers() = null
		})

		var sslContext: SSLContext? = null
		try {
			sslContext = SSLContext.getInstance("SSL").apply {
				this.init(null, trustAllCerts, SecureRandom())
			}
		} catch (exception: KeyManagementException) {
			exception.printStackTrace()
		} catch (exception: NoSuchAlgorithmException) {
			exception.printStackTrace()
		}

		sslContext?.let {
			this.okHttpClient?.sslSocketFactory = it.socketFactory
		}

		this.okHttpClient?.setHostnameVerifier { _, _ -> true }
	}

	companion object {
		val gson: Gson by lazy {
			GsonBuilder()
					.setExclusionStrategies(object : ExclusionStrategy {
						override fun shouldSkipField(f: FieldAttributes) = f.declaringClass == RealmObject::class.java

						override fun shouldSkipClass(clazz: Class<*>) = false
					})
					.create()
		}
		private val gsonConverter by lazy { GsonConverter(gson) }
		val instance by lazy { SickRageApi() }

		private fun buildApiUrl(useHttps: Boolean, address: String, portNumber: String): String {
			// Retrofit requires a non-empty endpoint
			// So we use the local url
			if (address.isEmpty()) {
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

		fun getCredentials(useBasicAuthentication: Boolean, username: String?, password: String?): String? {
			if (!useBasicAuthentication || username.isNullOrEmpty() || password.isNullOrEmpty()) {
				return null
			}

			return Credentials.basic(username, password)
		}

		fun getWebRoot(apiPath: String?): String {
			if (apiPath.isNullOrEmpty()) {
				return ""
			}

			var path = apiPath!!

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
