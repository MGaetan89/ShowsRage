package com.mgaetan89.showsrage.fragment

import android.widget.Spinner
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class AddShowOptionsFragment_GetLocationTest {
    @Parameterized.Parameter(1)
    var location: String? = null

    @Parameterized.Parameter(0)
    var spinner: Spinner? = null

    @Test
    fun getLocation() {
        assertThat(AddShowOptionsFragment.getLocation(this.spinner)).isEqualTo(this.location)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, null),
                    arrayOf<Any?>(getMockedSpinner(null), null),
                    arrayOf<Any?>(getMockedSpinner(""), ""),
                    arrayOf<Any?>(getMockedSpinner("/home/videos/Shows"), "/home/videos/Shows")
            )
        }

        private fun getMockedSpinner(selectedItem: String?): Spinner {
            val spinner = mock(Spinner::class.java)
            `when`(spinner.selectedItem).thenReturn(selectedItem)

            return spinner
        }
    }
}
