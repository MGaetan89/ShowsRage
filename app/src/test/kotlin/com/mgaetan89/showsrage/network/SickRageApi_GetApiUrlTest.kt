package com.mgaetan89.showsrage.network

import android.content.SharedPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Matchers.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class SickRageApi_GetApiUrlTest(
        val useHttps: Boolean, val address: String, val port: String,
        val path: String, val apiKey: String, val url: String
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
    fun getApiUrl() {
        assertThat(SickRageApi.instance.getApiUrl()).isEqualTo(this.url)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index} - {0}://{1}:{2}/{3}/{4}/")
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(false, "", "", "", "", "http://127.0.0.1/"),
                    arrayOf(false, "127.0.0.1", "", "", "", "http://127.0.0.1/"),
                    arrayOf(false, "127.0.0.1", "8083", "", "", "http://127.0.0.1:8083/"),
                    arrayOf(false, "127.0.0.1", "8083", "api", "", "http://127.0.0.1:8083/api/"),
                    arrayOf(false, "127.0.0.1", "8083", "/api", "", "http://127.0.0.1:8083/api/"),
                    arrayOf(false, "127.0.0.1", "8083", "api/", "", "http://127.0.0.1:8083/api/"),
                    arrayOf(false, "127.0.0.1", "8083", "/api/", "", "http://127.0.0.1:8083/api/"),
                    arrayOf(false, "127.0.0.1", "8083", "/api1/api2/", "", "http://127.0.0.1:8083/api1/api2/"),
                    arrayOf(false, "127.0.0.1", "8083", "api", "apiKey", "http://127.0.0.1:8083/api/apiKey/"),
                    arrayOf(true, "", "", "", "", "http://127.0.0.1/"),
                    arrayOf(true, "127.0.0.1", "", "", "", "https://127.0.0.1/"),
                    arrayOf(true, "127.0.0.1", "8083", "", "", "https://127.0.0.1:8083/"),
                    arrayOf(true, "127.0.0.1", "8083", "api", "", "https://127.0.0.1:8083/api/"),
                    arrayOf(true, "127.0.0.1", "8083", "/api", "", "https://127.0.0.1:8083/api/"),
                    arrayOf(true, "127.0.0.1", "8083", "api/", "", "https://127.0.0.1:8083/api/"),
                    arrayOf(true, "127.0.0.1", "8083", "/api/", "", "https://127.0.0.1:8083/api/"),
                    arrayOf(true, "127.0.0.1", "8083", "/api1/api2/", "", "https://127.0.0.1:8083/api1/api2/"),
                    arrayOf(true, "127.0.0.1", "8083", "api", "apiKey", "https://127.0.0.1:8083/api/apiKey/")
            )
        }
    }
}
