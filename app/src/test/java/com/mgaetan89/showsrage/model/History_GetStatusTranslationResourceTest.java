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
public class History_GetStatusTranslationResourceTest {
	@Parameterized.Parameter(0)
	public History history;

	@Parameterized.Parameter(1)
	public int statusTranslationResource;

	@Test
	public void getStatusTranslationResource() {
		assertThat(this.history.getStatusTranslationResource()).isEqualTo(this.statusTranslationResource);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{getJsonForStatus(gson, null), 0},
				{getJsonForStatus(gson, ""), 0},
				{getJsonForStatus(gson, "downloaded"), R.string.downloaded},
				{getJsonForStatus(gson, "Downloaded"), R.string.downloaded},
				{getJsonForStatus(gson, "snatched"), R.string.snatched},
				{getJsonForStatus(gson, "Snatched"), R.string.snatched},
				{gson.fromJson("{}", History.class), 0},
				{getJsonForStatus(gson, "status"), 0},
				{getJsonForStatus(gson, "Status"), 0},
		});
	}

	private static History getJsonForStatus(@NonNull Gson gson, @Nullable String status) {
		return gson.fromJson("{status: \"" + status + "\"}", History.class);
	}
}
