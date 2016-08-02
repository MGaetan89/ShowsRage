package com.mgaetan89.showsrage.network

import android.content.SharedPreferences
import com.mgaetan89.showsrage.extension.getApiKey
import com.mgaetan89.showsrage.extension.getPortNumber
import com.mgaetan89.showsrage.extension.getServerAddress
import com.mgaetan89.showsrage.extension.getServerPath
import com.mgaetan89.showsrage.extension.useHttps
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class SickRageApi_GetVideosUrlTest(
        val useHttps: Boolean, val address: String?, val port: String,
        val path: String, val apiKey: String, val url: String
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
    fun getVideosUrl() {
        assertThat(SickRageApi.instance.videosUrl).isEqualTo(this.url)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index} - {0}://{1}:{2}/{3}/{4}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(false, "", "", "", "", "http://127.0.0.1/videos"),
                    arrayOf(false, "127.0.0.1", "", "", "", "http://127.0.0.1/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "", "", "http://127.0.0.1:8083/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "api", "", "http://127.0.0.1:8083/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "/api", "", "http://127.0.0.1:8083/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "api/", "", "http://127.0.0.1:8083/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "/api/", "", "http://127.0.0.1:8083/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "/api1/api2/", "", "http://127.0.0.1:8083/videos"),
                    arrayOf(false, "127.0.0.1", "8083", "api", "apiKey", "http://127.0.0.1:8083/videos"),
                    arrayOf(true, "", "", "", "", "http://127.0.0.1/videos"),
                    arrayOf(true, "127.0.0.1", "", "", "", "https://127.0.0.1/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "", "", "https://127.0.0.1:8083/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "api", "", "https://127.0.0.1:8083/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "/api", "", "https://127.0.0.1:8083/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "api/", "", "https://127.0.0.1:8083/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "/api/", "", "https://127.0.0.1:8083/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "/api1/api2/", "", "https://127.0.0.1:8083/videos"),
                    arrayOf(true, "127.0.0.1", "8083", "api", "apiKey", "https://127.0.0.1:8083/videos")
            )
        }
    }
}
