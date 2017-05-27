package com.mgaetan89.showsrage.fragment

import android.widget.Spinner
import com.mgaetan89.showsrage.model.RootDir
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class AddShowOptionsFragment_GetLocationTest(val spinner: Spinner?, val location: String?) {
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

		private fun getMockedSpinner(location: String?): Spinner {
			var rootDir: RootDir? = null

			if (location != null) {
				rootDir = RootDir()
				rootDir.location = location
			}

			val spinner = mock(Spinner::class.java)
			`when`(spinner.selectedItem).thenReturn(rootDir)

			return spinner
		}
	}
}
