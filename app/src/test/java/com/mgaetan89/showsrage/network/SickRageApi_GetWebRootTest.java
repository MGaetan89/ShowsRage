package com.mgaetan89.showsrage.network;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SickRageApi_GetWebRootTest {
	@Parameterized.Parameter(0)
	public String apiPath;

	@Parameterized.Parameter(1)
	public String webRoot;

	@Test
	public void getWebRoot() {
		assertThat(SickRageApi.getWebRoot(this.apiPath)).isEqualTo(this.webRoot);
	}

	@Parameterized.Parameters(name = "{index}: {0} -> {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, ""},
				{"", ""},
				{"api", ""},
				{"/api", ""},
				{"/api/", ""},
				{"foo/api", "foo/"},
				{"foo/api/", "foo/"},
				{"/foo/api", "foo/"},
				{"/foo/api/", "foo/"},
				{"api/bar", "api/bar/"},
				{"api/bar/", "api/bar/"},
				{"/api/bar", "api/bar/"},
				{"/api/bar/", "api/bar/"},
				{"foo/api/bar", "foo/api/bar/"},
				{"foo/api/bar/", "foo/api/bar/"},
				{"/foo/api/bar", "foo/api/bar/"},
				{"/foo/api/bar/", "foo/api/bar/"},
		});
	}
}
