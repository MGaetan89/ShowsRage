package com.mgaetan89.showsrage.fragment

import android.content.res.Resources
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.widget.Spinner
import com.mgaetan89.showsrage.EmptyFragmentHostCallback
import com.mgaetan89.showsrage.R
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.*

@RunWith(Parameterized::class)
class AddShowOptionsFragment_GetLanguageTest {
    @Parameterized.Parameter(1)
    var language: String? = null

    @Parameterized.Parameter(0)
    var spinner: Spinner? = null

    private lateinit var fragment: AddShowOptionsFragment

    @Before
    fun before() {
        val resources = mock(Resources::class.java)
        `when`(resources.getStringArray(R.array.languages_keys)).thenReturn(arrayOf("en", "fr", "cs", "da", "de", "el", "es", "fi", "he", "hr", "hu", "it", "ja", "ko", "nl", "no", "pl", "pt", "ru", "sl", "sv", "tr", "zh"))

        val activity = mock(FragmentActivity::class.java)
        `when`(activity.resources).thenReturn(resources)

        this.fragment = spy(AddShowOptionsFragment())

        try {
            val fragmentHostField = Fragment::class.java.getDeclaredField("mHost")
            fragmentHostField.isAccessible = true
            fragmentHostField.set(this.fragment, EmptyFragmentHostCallback(activity))
        } catch (ignored: IllegalAccessException) {
        } catch (ignored: NoSuchFieldException) {
        }

        `when`(this.fragment.resources).thenReturn(resources)
    }

    @Test
    fun getLanguage() {
        assertThat(this.fragment.getLanguage(this.spinner)).isEqualTo(this.language)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, "en"),
                    arrayOf<Any?>(getMockedSpinner(-1), null),
                    arrayOf<Any?>(getMockedSpinner(0), "en"),
                    arrayOf<Any?>(getMockedSpinner(1), "fr"),
                    arrayOf<Any?>(getMockedSpinner(2), "cs"),
                    arrayOf<Any?>(getMockedSpinner(3), "da"),
                    arrayOf<Any?>(getMockedSpinner(4), "de"),
                    arrayOf<Any?>(getMockedSpinner(5), "el"),
                    arrayOf<Any?>(getMockedSpinner(6), "es"),
                    arrayOf<Any?>(getMockedSpinner(7), "fi"),
                    arrayOf<Any?>(getMockedSpinner(8), "he"),
                    arrayOf<Any?>(getMockedSpinner(9), "hr"),
                    arrayOf<Any?>(getMockedSpinner(10), "hu"),
                    arrayOf<Any?>(getMockedSpinner(11), "it"),
                    arrayOf<Any?>(getMockedSpinner(12), "ja"),
                    arrayOf<Any?>(getMockedSpinner(13), "ko"),
                    arrayOf<Any?>(getMockedSpinner(14), "nl"),
                    arrayOf<Any?>(getMockedSpinner(15), "no"),
                    arrayOf<Any?>(getMockedSpinner(16), "pl"),
                    arrayOf<Any?>(getMockedSpinner(17), "pt"),
                    arrayOf<Any?>(getMockedSpinner(18), "ru"),
                    arrayOf<Any?>(getMockedSpinner(19), "sl"),
                    arrayOf<Any?>(getMockedSpinner(20), "sv"),
                    arrayOf<Any?>(getMockedSpinner(21), "tr"),
                    arrayOf<Any?>(getMockedSpinner(22), "zh"),
                    arrayOf<Any?>(getMockedSpinner(23), null)
            )
        }

        private fun getMockedSpinner(selectedItemPosition: Int): Spinner {
            val spinner = mock(Spinner::class.java)
            `when`(spinner.selectedItemPosition).thenReturn(selectedItemPosition)

            return spinner
        }
    }
}
