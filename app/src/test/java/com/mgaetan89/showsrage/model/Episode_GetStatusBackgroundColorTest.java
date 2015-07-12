package com.mgaetan89.showsrage.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Episode_GetStatusBackgroundColorTest {
	@Parameterized.Parameter(1)
	public int color;

	@Parameterized.Parameter(0)
	public Episode episode;

	@Test
	public void getStatusBackgroundColor() {
		assertThat(this.episode.getStatusBackgroundColor()).isEqualTo(this.color);
	}

	@Parameterized.Parameters(name = "{index}: {0} - {1}")
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{getJsonForStatus(gson, "archived"), R.color.green},
				{getJsonForStatus(gson, "Archived"), R.color.green},
				{getJsonForStatus(gson, "downloaded"), R.color.green},
				{getJsonForStatus(gson, "Downloaded"), R.color.green},
				{getJsonForStatus(gson, "ignored"), R.color.blue},
				{getJsonForStatus(gson, "Ignored"), R.color.blue},
				{getJsonForStatus(gson, "skipped"), R.color.blue},
				{getJsonForStatus(gson, "Skipped"), R.color.blue},
				{getJsonForStatus(gson, "snatched"), R.color.purple},
				{getJsonForStatus(gson, "Snatched"), R.color.purple},
				{getJsonForStatus(gson, "unaired"), R.color.yellow},
				{getJsonForStatus(gson, "Unaired"), R.color.yellow},
				{getJsonForStatus(gson, "wanted"), R.color.red},
				{getJsonForStatus(gson, "Wanted"), R.color.red},
				{gson.fromJson("{}", Episode.class), android.R.color.transparent},
				{getJsonForStatus(gson, null), android.R.color.transparent},
				{getJsonForStatus(gson, ""), android.R.color.transparent},
				{getJsonForStatus(gson, "zstatusz"), android.R.color.transparent},
				{getJsonForStatus(gson, "ZStatusZ"), android.R.color.transparent},
		});
	}

	private static Episode getJsonForStatus(@NonNull Gson gson, @Nullable String status) {
		return gson.fromJson("{status: \"" + status + "\"}", Episode.class);
	}
}
