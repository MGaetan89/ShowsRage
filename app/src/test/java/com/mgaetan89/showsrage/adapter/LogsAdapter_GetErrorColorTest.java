package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class LogsAdapter_GetErrorColorTest {
	@Parameterized.Parameter(1)
	public int color;

	@Parameterized.Parameter(0)
	public String errorType;

	@Test
	public void getErrorColor() {
		assertThat(LogsAdapter.getErrorColor(this.errorType)).isEqualTo(this.color);
	}

	@Parameterized.Parameters(name = "{index}: {0} - {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, android.R.color.black},
				{"", android.R.color.black},
				{"debug", R.color.green},
				{"Debug", R.color.green},
				{"error", R.color.red},
				{"Error", R.color.red},
				{"info", R.color.blue},
				{"Info", R.color.blue},
				{"warning", R.color.orange},
				{"Warning", R.color.orange},
				{"errortype", android.R.color.black},
				{"ErrorType", android.R.color.black},
		});
	}
}
