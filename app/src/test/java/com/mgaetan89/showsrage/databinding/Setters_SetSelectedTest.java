package com.mgaetan89.showsrage.databinding;

import android.widget.TextView;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class Setters_SetSelectedTest {
	@Test
	public void setSelectedFalse() {
		TextView textView = mock(TextView.class);

		Setters.setSelected(textView, false);

		verify(textView).setSelected(false);
	}

	@Test
	public void setSelectedTrue() {
		TextView textView = mock(TextView.class);

		Setters.setSelected(textView, true);

		verify(textView).setSelected(true);
	}
}
