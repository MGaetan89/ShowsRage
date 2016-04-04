package com.mgaetan89.showsrage.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class Episode_GetStatusTranslationResourceTest {
	@Parameterized.Parameter(1)
	public int statusTranslationResource;

	@Parameterized.Parameter(0)
	public Episode episode;

	@Test
	public void getStatusTranslationResource() {
		assertThat(this.episode.getStatusTranslationResource()).isEqualTo(this.statusTranslationResource);
	}

	@Parameterized.Parameters(name = "{index}: {0} - {1}")
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{getJsonForStatus(gson, "archived"), R.string.archived},
				{getJsonForStatus(gson, "Archived"), R.string.archived},
				{getJsonForStatus(gson, "downloaded"), R.string.downloaded},
				{getJsonForStatus(gson, "Downloaded"), R.string.downloaded},
				{getJsonForStatus(gson, "ignored"), R.string.ignored},
				{getJsonForStatus(gson, "Ignored"), R.string.ignored},
				{getJsonForStatus(gson, "skipped"), R.string.skipped},
				{getJsonForStatus(gson, "Skipped"), R.string.skipped},
				{getJsonForStatus(gson, "snatched"), R.string.snatched},
				{getJsonForStatus(gson, "Snatched"), R.string.snatched},
				{getJsonForStatus(gson, "snatched (proper)"), R.string.snatched_proper},
				{getJsonForStatus(gson, "Snatched (proper)"), R.string.snatched_proper},
				{getJsonForStatus(gson, "unaired"), R.string.unaired},
				{getJsonForStatus(gson, "Unaired"), R.string.unaired},
				{getJsonForStatus(gson, "wanted"), R.string.wanted},
				{getJsonForStatus(gson, "Wanted"), R.string.wanted},
				{gson.fromJson("{}", Episode.class), 0},
				{getJsonForStatus(gson, null), 0},
				{getJsonForStatus(gson, ""), 0},
				{getJsonForStatus(gson, "zstatusz"), 0},
				{getJsonForStatus(gson, "ZStatusZ"), 0},
		});
	}

	private static Episode getJsonForStatus(@NonNull Gson gson, @Nullable String status) {
		return gson.fromJson("{status: \"" + status + "\"}", Episode.class);
	}
}
