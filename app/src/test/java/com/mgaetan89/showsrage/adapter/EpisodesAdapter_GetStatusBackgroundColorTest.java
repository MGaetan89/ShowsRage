package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class EpisodesAdapter_GetStatusBackgroundColorTest {
	@Parameterized.Parameter(1)
	public int color;

	@Parameterized.Parameter(0)
	public String status;

	@Test
	public void getStatusBackgroundColor() {
		assertThat(EpisodesAdapter.getStatusBackgroundColor(this.status)).isEqualTo(this.color);
	}

	@Parameterized.Parameters(name = "{index}: {0} - {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"archived", R.color.green},
				{"Archived", R.color.green},
				{"downloaded", R.color.green},
				{"Downloaded", R.color.green},
				{"snatched", R.color.purple},
				{"Snatched", R.color.purple},
				{"unaired", R.color.yellow},
				{"Unaired", R.color.yellow},
				{"wanted", R.color.red},
				{"Wanted", R.color.red},
				{null, android.R.color.transparent},
				{"", android.R.color.transparent},
				{"zstatusz", android.R.color.transparent},
				{"ZStatusZ", android.R.color.transparent},
		});
	}
}
