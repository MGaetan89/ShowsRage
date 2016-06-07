package com.mgaetan89.showsrage.network

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SickRageApi_GetWebRootTest(val apiPath: String?, val webRoot: String) {
    @Test
    fun getWebRoot() {
        assertThat(SickRageApi.getWebRoot(this.apiPath)).isEqualTo(this.webRoot)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} -> {1}")
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, ""),
                    arrayOf<Any?>("", ""),
                    arrayOf<Any?>("api", ""),
                    arrayOf<Any?>("/api", ""),
                    arrayOf<Any?>("/api/", ""),
                    arrayOf<Any?>("foo/api", "foo/"),
                    arrayOf<Any?>("foo/api/", "foo/"),
                    arrayOf<Any?>("/foo/api", "foo/"),
                    arrayOf<Any?>("/foo/api/", "foo/"),
                    arrayOf<Any?>("api/bar", "api/bar/"),
                    arrayOf<Any?>("api/bar/", "api/bar/"),
                    arrayOf<Any?>("/api/bar", "api/bar/"),
                    arrayOf<Any?>("/api/bar/", "api/bar/"),
                    arrayOf<Any?>("foo/api/bar", "foo/api/bar/"),
                    arrayOf<Any?>("foo/api/bar/", "foo/api/bar/"),
                    arrayOf<Any?>("/foo/api/bar", "foo/api/bar/"),
                    arrayOf<Any?>("/foo/api/bar/", "foo/api/bar/")
            )
        }
    }
}
