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
public class Show_GetStatusTranslationResourceTest {
	@Parameterized.Parameter(0)
	public Show show;

	@Parameterized.Parameter(1)
	public int statusTranslationResource;

	@Test
	public void getStatusTranslationResource() {
		assertThat(this.show.getStatusTranslationResource()).isEqualTo(this.statusTranslationResource);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = new Gson();

		return Arrays.asList(new Object[][]{
				{getJsonForStatus(gson, null), 0},
				{getJsonForStatus(gson, ""), 0},
				{getJsonForStatus(gson, "continuing"), R.string.continuing},
				{getJsonForStatus(gson, "Continuing"), R.string.continuing},
				{getJsonForStatus(gson, "ended"), R.string.ended},
				{getJsonForStatus(gson, "Ended"), R.string.ended},
				{gson.fromJson("{}", Show.class), 0},
				{getJsonForStatus(gson, "status"), 0},
				{getJsonForStatus(gson, "Status"), 0},
		});
	}

	private static Show getJsonForStatus(@NonNull Gson gson, @Nullable String status) {
		return gson.fromJson("{status: \"" + status + "\"}", Show.class);
	}
}
