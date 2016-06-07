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
class AddShowOptionsFragment_GetPreferredQualityTest(val spinner: Spinner?, val preferredQuality: String?) {
    private lateinit var fragment: AddShowOptionsFragment

    @Before
    fun before() {
        val resources = mock(Resources::class.java)
        `when`(resources.getStringArray(R.array.preferred_qualities_keys)).thenReturn(arrayOf("sddvd", "hdtv", "rawhdtv", "fullhdtv", "hdwebdl", "fullhdwebdl", "hdbluray", "fullhdbluray"))

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
    fun getPreferredQuality() {
        assertThat(this.fragment.getPreferredQuality(this.spinner)).isEqualTo(this.preferredQuality)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, "sddvd"),
                    arrayOf<Any?>(getMockedSpinner(-1), null),
                    arrayOf<Any?>(getMockedSpinner(0), null),
                    arrayOf<Any?>(getMockedSpinner(1), "sddvd"),
                    arrayOf<Any?>(getMockedSpinner(2), "hdtv"),
                    arrayOf<Any?>(getMockedSpinner(3), "rawhdtv"),
                    arrayOf<Any?>(getMockedSpinner(4), "fullhdtv"),
                    arrayOf<Any?>(getMockedSpinner(5), "hdwebdl"),
                    arrayOf<Any?>(getMockedSpinner(6), "fullhdwebdl"),
                    arrayOf<Any?>(getMockedSpinner(7), "hdbluray"),
                    arrayOf<Any?>(getMockedSpinner(8), "fullhdbluray"),
                    arrayOf<Any?>(getMockedSpinner(9), null)
            )
        }

        private fun getMockedSpinner(selectedItemPosition: Int): Spinner {
            val spinner = mock(Spinner::class.java)
            `when`(spinner.selectedItemPosition).thenReturn(selectedItemPosition)

            return spinner
        }
    }
}
