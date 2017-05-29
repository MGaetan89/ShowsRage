package com.mgaetan89.showsrage.network

import com.squareup.okhttp.Protocol
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.IOException
import java.net.Proxy

@RunWith(Parameterized::class)
class SickRageApi_GetOkHttpClientTest(val useSelfSignedCertificate: Boolean, val useBasicAuthentication: Boolean) {
	@Before
	fun before() {
		if (this.useBasicAuthentication) {
			SickRageApi.setCredentials("dXNlcm5hbWU6cGFzc3dvcmQ=")
		} else {
			SickRageApi.setCredentials(null)
		}
	}

	@Test
	@Throws(IOException::class)
	fun getOkHttpClient() {
		val client = SickRageApi.instance.getOkHttpClient(this.useSelfSignedCertificate)
		val response = Response.Builder().code(200).protocol(Protocol.HTTP_2).request(
				Request.Builder().url("http://www.google.com/").build()).build()

		assertThat(client).isNotNull()
		assertThat(client.authenticator).isNotNull()

		if (this.useBasicAuthentication) {
			assertThat(client.authenticator.authenticate(Proxy.NO_PROXY, response)).isNotNull()
		} else {
			assertThat(client.authenticator.authenticate(Proxy.NO_PROXY, response)).isNull()
		}

		if (this.useSelfSignedCertificate) {
			assertThat(client.hostnameVerifier).isNotNull()
			assertThat(client.hostnameVerifier.verify("", null)).isTrue()
			assertThat(client.sslSocketFactory).isNotNull()
		} else {
			assertThat(client.hostnameVerifier).isNull()
			assertThat(client.sslSocketFactory).isNull()
		}
	}

	@After
	fun after() {
		SickRageApi.setCredentials(null)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters(name = "{index}: {0} -> {1}")
		fun data(): Collection<Array<Any>> {
			return listOf(
					arrayOf<Any>(false, false),
					arrayOf<Any>(false, true),
					arrayOf<Any>(true, false),
					arrayOf<Any>(true, true)
			)
		}
	}
}
