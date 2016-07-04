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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy

@RunWith(Parameterized::class)
class AddShowOptionsFragment_GetStatusTest(val spinner: Spinner?, val status: String?) {
    private lateinit var fragment: AddShowOptionsFragment

    @Before
    fun before() {
        val resources = mock(Resources::class.java)
        `when`(resources.getStringArray(R.array.status_keys)).thenReturn(arrayOf("wanted", "skipped", "archived", "ignored"))

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
    fun getStatus() {
        assertThat(this.fragment.getStatus(this.spinner)).isEqualTo(this.status)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, "wanted"),
                    arrayOf<Any?>(getMockedSpinner(-1), null),
                    arrayOf<Any?>(getMockedSpinner(0), "wanted"),
                    arrayOf<Any?>(getMockedSpinner(1), "skipped"),
                    arrayOf<Any?>(getMockedSpinner(2), "archived"),
                    arrayOf<Any?>(getMockedSpinner(3), "ignored"),
                    arrayOf<Any?>(getMockedSpinner(4), null)
            )
        }

        private fun getMockedSpinner(selectedItemPosition: Int): Spinner {
            val spinner = mock(Spinner::class.java)
            `when`(spinner.selectedItemPosition).thenReturn(selectedItemPosition)

            return spinner
        }
    }
}
