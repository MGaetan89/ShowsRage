package com.mgaetan89.showsrage.fragment;

import com.google.gson.Gson;
import com.mgaetan89.showsrage.model.Show;
import com.mgaetan89.showsrage.network.SickRageApi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ShowsSectionFragment_IsShowValidTest {
	@Parameterized.Parameter(1)
	public boolean valid;

	@Parameterized.Parameter(0)
	public Show show;

	@Test
	public void isShowValid() {
		assertThat(ShowsSectionFragment.Companion.isShowValid(this.show)).isEqualTo(this.valid);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Gson gson = SickRageApi.Companion.getGson();

		return Arrays.asList(new Object[][]{
				{null, false},
				{gson.fromJson("{}", Show.class), false},
				{gson.fromJson("{indexerid: 0}", Show.class), false},
				{gson.fromJson("{indexerid: 123}", Show.class), true},
		});
	}
}
