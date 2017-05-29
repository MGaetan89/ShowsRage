package com.mgaetan89.showsrage.network

import android.content.SharedPreferences
import com.mgaetan89.showsrage.extension.getApiKey
import com.mgaetan89.showsrage.extension.getPortNumber
import com.mgaetan89.showsrage.extension.getServerAddress
import com.mgaetan89.showsrage.extension.getServerPath
import com.mgaetan89.showsrage.extension.useHttps
import com.mgaetan89.showsrage.model.Indexer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class SickRageApi_GetBannerUrlTest(
		val useHttps: Boolean, val address: String, val port: String, val path: String,
		val apiKey: String, val indexerId: Int, val indexer: Indexer?, val url: String
) {
	@Before
	fun before() {
		val preferences = mock(SharedPreferences::class.java)
		`when`(preferences.useHttps()).thenReturn(this.useHttps)
		`when`(preferences.getServerAddress()).thenReturn(this.address)
		`when`(preferences.getPortNumber()).thenReturn(this.port)
		`when`(preferences.getServerPath()).thenReturn(this.path)
		`when`(preferences.getApiKey()).thenReturn(this.apiKey)

		SickRageApi.instance.init(preferences)
	}

	@Test
	fun getBannerUrl() {
		assertThat(SickRageApi.instance.getBannerUrl(this.indexerId, this.indexer)).isEqualTo(this.url)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters(name = "[{6}] {index} - {0}://{1}:{2}/{3}/{4}/")
		fun data(): Collection<Array<Any?>> {
			return listOf(
					// TVDB
					arrayOf(false, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getbanner"),
					arrayOf(false, "127.0.0.1", "", "", "", 123, null, "http://127.0.0.1/?cmd=show.getbanner"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "", "", 0, Indexer.TVDB, "http://127.0.0.1:8083/?cmd=show.getbanner&tvdbid=0"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api1/api2/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=123"),
					arrayOf(true, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getbanner"),
					arrayOf(true, "127.0.0.1", "", "", "", 123, null, "https://127.0.0.1/?cmd=show.getbanner"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "", "", 0, Indexer.TVDB, "https://127.0.0.1:8083/?cmd=show.getbanner&tvdbid=0"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api1/api2/?cmd=show.getbanner&tvdbid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvdbid=123"),

					// TVRage
					arrayOf(false, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getbanner"),
					arrayOf(false, "127.0.0.1", "", "", "", 123, null, "http://127.0.0.1/?cmd=show.getbanner"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "", "", 0, Indexer.TVRAGE, "http://127.0.0.1:8083/?cmd=show.getbanner&tvrageid=0"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api1/api2/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvrageid=123"),
					arrayOf(true, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getbanner"),
					arrayOf(true, "127.0.0.1", "", "", "", 123, null, "https://127.0.0.1/?cmd=show.getbanner"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "", "", 0, Indexer.TVRAGE, "https://127.0.0.1:8083/?cmd=show.getbanner&tvrageid=0"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api1/api2/?cmd=show.getbanner&tvrageid=123"),
					arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getbanner&tvrageid=123")
			)
		}
	}
}
