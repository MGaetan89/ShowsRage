package com.mgaetan89.showsrage.network;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SickRageApi_GetApiPathTest {
	@Parameterized.Parameter(0)
	public String apiPath;

	@Parameterized.Parameter(1)
	public String expected;

	@Test
	public void getApiPath() {
		assertThat(SickRageApi.getApiPath(this.apiPath)).isEqualTo(this.expected);
	}

	@Parameterized.Parameters(name = "{index}: {0} -> {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, ""},
				{"", ""},
				{"/", ""},
				{"foo", "foo"},
				{"/foo", "foo"},
				{"/foo/", "foo"},
				{"foo/bar", "foo/bar"},
				{"foo/bar/", "foo/bar"},
				{"/foo/bar", "foo/bar"},
				{"/foo/bar/", "foo/bar"},
		});
	}
}
