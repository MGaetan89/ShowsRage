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
class PostProcessingFragment_GetProcessingMethodsTest(val spinner: Spinner?, val processingMethod: String?) {
    private lateinit var fragment: PostProcessingFragment

    @Before
    fun before() {
        val resources = mock(Resources::class.java)
        `when`(resources.getStringArray(R.array.processing_methods_values)).thenReturn(arrayOf("", "copy", "move", "hardlink", "symlink"))

        val activity = mock(FragmentActivity::class.java)
        `when`(activity.resources).thenReturn(resources)

        this.fragment = spy(PostProcessingFragment())

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
    fun getProcessingMethod() {
        assertThat(this.fragment.getProcessingMethod(this.spinner)).isEqualTo(this.processingMethod)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, null),
                    arrayOf<Any?>(getMockedSpinner(-1), null),
                    arrayOf<Any?>(getMockedSpinner(0), null),
                    arrayOf<Any?>(getMockedSpinner(1), "copy"),
                    arrayOf<Any?>(getMockedSpinner(2), "move"),
                    arrayOf<Any?>(getMockedSpinner(3), "hardlink"),
                    arrayOf<Any?>(getMockedSpinner(4), "symlink"),
                    arrayOf<Any?>(getMockedSpinner(5), null)
            )
        }

        private fun getMockedSpinner(selectedItemPosition: Int): Spinner {
            val spinner = mock(Spinner::class.java)
            `when`(spinner.selectedItemPosition).thenReturn(selectedItemPosition)

            return spinner
        }
    }
}
