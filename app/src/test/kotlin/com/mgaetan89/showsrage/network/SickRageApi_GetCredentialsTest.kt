package com.mgaetan89.showsrage.network

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SickRageApi_GetCredentialsTest(val useBasicAuthentication: Boolean, val username: String?, val password: String?, val credentials: String?) {
    @Test
    fun getCredentials() {
        assertThat(SickRageApi.getCredentials(this.useBasicAuthentication, this.username, this.password)).isEqualTo(this.credentials)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0} [{1}:{2}]")
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(false, null, null, null),
                    arrayOf(false, null, "", null),
                    arrayOf(false, null, "password", null),
                    arrayOf(false, "", "password", null),
                    arrayOf(false, "username", "password", null),
                    arrayOf(false, "username", "", null),
                    arrayOf(false, "username", null, null),
                    arrayOf<Any?>(true, null, null, null),
                    arrayOf(true, null, "", null),
                    arrayOf(true, null, "password", null),
                    arrayOf(true, "", "password", null),
                    arrayOf<Any?>(true, "username", "password", "Basic dXNlcm5hbWU6cGFzc3dvcmQ="),
                    arrayOf(true, "username", "", null),
                    arrayOf(true, "username", null, null)
            )
        }
    }
}
