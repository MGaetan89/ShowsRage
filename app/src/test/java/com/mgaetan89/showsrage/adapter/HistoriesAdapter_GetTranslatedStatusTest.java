package com.mgaetan89.showsrage.adapter;

import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class HistoriesAdapter_GetTranslatedStatusTest {
	@Parameterized.Parameter(0)
	public String status;

	@Parameterized.Parameter(1)
	public int translatedStatus;

	@Test
	public void getTranslatedStatus() {
		assertThat(HistoriesAdapter.getTranslatedStatus(this.status)).isEqualTo(this.translatedStatus);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, 0},
				{"", 0},
				{"downloaded", R.string.downloaded},
				{"Downloaded", R.string.downloaded},
				{"snatched", R.string.snatched},
				{"Snatched", R.string.snatched},
				{"status", 0},
				{"Status", 0},
		});
	}
}
