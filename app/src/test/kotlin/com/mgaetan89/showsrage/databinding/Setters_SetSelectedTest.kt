package com.mgaetan89.showsrage.databinding

import android.widget.TextView

import org.junit.Test

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class Setters_SetSelectedTest {
	@Test
	fun setSelectedFalse() {
		val textView = mock(TextView::class.java)

		setSelected(textView, false)

		verify(textView).isSelected = false
	}

	@Test
	fun setSelectedTrue() {
		val textView = mock(TextView::class.java)

		setSelected(textView, true)

		verify(textView).isSelected = true
	}
}
