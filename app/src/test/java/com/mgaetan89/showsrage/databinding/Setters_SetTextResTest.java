package com.mgaetan89.showsrage.databinding;

import android.widget.TextView;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class Setters_SetTextResTest {
	@Parameterized.Parameter(1)
	public Object text;

	@Parameterized.Parameter(0)
	public int textRes;

	@Test
	public void setTextRes() {
		TextView textView = mock(TextView.class);

		SettersKt.setTextRes(textView, this.textRes);

		if (this.text instanceof String) {
			verify(textView).setText(this.text.toString());
		} else if (this.text instanceof Integer) {
			verify(textView).setText((int) this.text);
		} else {
			fail("text is not valid");
		}
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{R.string.app_name, R.string.app_name},
				{0, ""},
		});
	}
}
