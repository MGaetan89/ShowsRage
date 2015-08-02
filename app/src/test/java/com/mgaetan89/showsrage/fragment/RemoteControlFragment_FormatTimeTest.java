package com.mgaetan89.showsrage.fragment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class RemoteControlFragment_FormatTimeTest {
	@Parameterized.Parameter(1)
	public String formattedTime;

	@Parameterized.Parameter(0)
	public long time;

	@Test
	public void getQueryFromIntent() {
		assertThat(RemoteControlFragment.formatTime(this.time)).isEqualTo(this.formattedTime);
	}

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{-42L, "00:00"},
				{0L, "00:00"},
				{1_000L, "00:01"},
				{5_000L, "00:05"},
				{15_000L, "00:15"},
				{30_000L, "00:30"},
				{45_000L, "00:45"},
				{60_000L, "01:00"},
				{75_000L, "01:15"},
				{90_000L, "01:30"},
				{105_000L, "01:45"},
				{120_000L, "02:00"},
				{3_600_000L, "01:00:00"},
				{3_660_000L, "01:01:00"},
				{3_661_000L, "01:01:01"},
		});
	}
}
