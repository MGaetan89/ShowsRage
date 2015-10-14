package com.mgaetan89.showsrage.databinding;

import android.content.Context;
import android.content.res.Resources;
import android.widget.TextView;

import com.mgaetan89.showsrage.R;

import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class Setters_SetTextColorResTest {
	@Test
	public void setTextColorRes() {
		Context context = mock(Context.class);
		when(context.getResources()).thenReturn(mock(Resources.class));

		TextView textView = mock(TextView.class);
		when(textView.getContext()).thenReturn(context);

		Setters.setTextColorRes(textView, R.color.primary);

		verify(textView).setTextColor(anyInt());
	}
}
