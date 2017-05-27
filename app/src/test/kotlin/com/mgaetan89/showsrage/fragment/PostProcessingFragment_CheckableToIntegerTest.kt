package com.mgaetan89.showsrage.fragment

import android.widget.Checkable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class PostProcessingFragment_CheckableToIntegerTest(val checkable: Checkable?, val checked: Boolean, val result: Int) {
	@Before
	fun before() {
		if (this.checkable != null) {
			this.checkable.isChecked = this.checked

			`when`(this.checkable.isChecked).thenReturn(this.checked)
		}
	}

	@Test
	fun checkableToInteger() {
		assertThat(PostProcessingFragment.checkableToInteger(this.checkable)).isEqualTo(this.result)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			return listOf(
					arrayOf(null, false, 0),
					arrayOf(null, true, 0),
					arrayOf<Any?>(mock(Checkable::class.java), false, 0),
					arrayOf<Any?>(mock(Checkable::class.java), true, 1)
			)
		}
	}
}
