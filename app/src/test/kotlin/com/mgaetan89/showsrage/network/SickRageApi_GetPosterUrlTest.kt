package com.mgaetan89.showsrage.network

import android.content.SharedPreferences
import com.mgaetan89.showsrage.model.Indexer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Matchers.anyBoolean
import org.mockito.Matchers.anyString
import org.mockito.Matchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class SickRageApi_GetPosterUrlTest(
        val useHttps: Boolean, val address: String, val port: String, val path: String,
        val apiKey: String, val indexerId: Int, val indexer: Indexer?, val url: String
) {
    @Before
    fun before() {
        val preferences = mock(SharedPreferences::class.java)
        `when`(preferences.getBoolean(eq("use_https"), anyBoolean())).thenReturn(this.useHttps)
        `when`(preferences.getString(eq("server_address"), anyString())).thenReturn(this.address)
        `when`(preferences.getString(eq("server_port_number"), anyString())).thenReturn(this.port)
        `when`(preferences.getString(eq("server_path"), anyString())).thenReturn(this.path)
        `when`(preferences.getString(eq("api_key"), anyString())).thenReturn(this.apiKey)

        SickRageApi.instance.init(preferences)
    }

    @Test
    fun getPosterUrl() {
        assertThat(SickRageApi.instance.getPosterUrl(this.indexerId, this.indexer)).isEqualTo(this.url)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "[{6}] {index} - {0}://{1}:{2}/{3}/{4}/")
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    // TVDB
                    arrayOf(false, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"),
                    arrayOf(false, "127.0.0.1", "", "", "", 123, null, "http://127.0.0.1/?cmd=show.getposter"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "", "", 0, Indexer.TVDB, "http://127.0.0.1:8083/?cmd=show.getposter&tvdbid=0"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123"),
                    arrayOf(true, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"),
                    arrayOf(true, "127.0.0.1", "", "", "", 123, null, "https://127.0.0.1/?cmd=show.getposter"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "", "", 0, Indexer.TVDB, "https://127.0.0.1:8083/?cmd=show.getposter&tvdbid=0"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvdbid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123"),

                    // TVRage
                    arrayOf(false, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"),
                    arrayOf(false, "127.0.0.1", "", "", "", 123, null, "http://127.0.0.1/?cmd=show.getposter"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "", "", 0, Indexer.TVRAGE, "http://127.0.0.1:8083/?cmd=show.getposter&tvrageid=0"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(false, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvrageid=123"),
                    arrayOf(true, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"),
                    arrayOf(true, "127.0.0.1", "", "", "", 123, null, "https://127.0.0.1/?cmd=show.getposter"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "", "", 0, Indexer.TVRAGE, "https://127.0.0.1:8083/?cmd=show.getposter&tvrageid=0"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvrageid=123"),
                    arrayOf<Any?>(true, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvrageid=123")
            )
        }
    }
}
