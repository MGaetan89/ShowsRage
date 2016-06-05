package com.mgaetan89.showsrage.network

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SickRageApi_GetApiPathTest {
    @Parameterized.Parameter(0)
    var apiPath: String? = null

    @Parameterized.Parameter(1)
    var expected: String? = null

    @Test
    fun getApiPath() {
        assertThat(SickRageApi.getApiPath(this.apiPath)).isEqualTo(this.expected)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} -> {1}")
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, ""),
                    arrayOf<Any?>("", ""),
                    arrayOf<Any?>("/", ""),
                    arrayOf<Any?>("foo", "foo"),
                    arrayOf<Any?>("/foo", "foo"),
                    arrayOf<Any?>("/foo/", "foo"),
                    arrayOf<Any?>("foo/bar", "foo/bar"),
                    arrayOf<Any?>("foo/bar/", "foo/bar"),
                    arrayOf<Any?>("/foo/bar", "foo/bar"),
                    arrayOf<Any?>("/foo/bar/", "foo/bar")
            )
        }
    }
}
