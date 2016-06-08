package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class ShowFragment_GetSeasonsSortTest(val status: Boolean, val sort: String) {
    private lateinit var preferences: SharedPreferences

    @Before
    fun before() {
        this.preferences = mock(SharedPreferences::class.java)
        `when`(this.preferences.getBoolean("display_seasons_sort", false)).thenReturn(this.status)
    }

    @Test
    fun getSeasonsSort() {
        val sort = ShowFragment.getSeasonsSort(this.preferences)

        assertThat(sort).isEqualTo(this.sort)
    }

    @Test
    fun getSeasonsSortNull() {
        val sort = ShowFragment.getSeasonsSort(null)

        assertThat(sort).isEqualTo("desc")
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                    arrayOf(false, "desc"),
                    arrayOf(true, "asc")
            )
        }
    }
}
