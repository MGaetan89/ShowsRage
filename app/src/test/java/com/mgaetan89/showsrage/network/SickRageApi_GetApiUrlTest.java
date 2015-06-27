package com.mgaetan89.showsrage.network;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class SickRageApi_GetApiUrlTest {
	@Parameterized.Parameter(1)
	public String address;

	@Parameterized.Parameter(4)
	public String apiKey;

	@Parameterized.Parameter(3)
	public String path;

	@Parameterized.Parameter(2)
	public String port;

	@Parameterized.Parameter(5)
	public String url;

	@Parameterized.Parameter(0)
	public boolean useHttps;

	@Before
	public void before() {
		SharedPreferences preferences = mock(SharedPreferences.class);
		when(preferences.getBoolean(eq("use_https"), anyBoolean())).thenReturn(this.useHttps);
		when(preferences.getString(eq("server_address"), anyString())).thenReturn(this.address);
		when(preferences.getString(eq("server_port_number"), anyString())).thenReturn(this.port);
		when(preferences.getString(eq("server_path"), anyString())).thenReturn(this.path);
		when(preferences.getString(eq("api_key"), anyString())).thenReturn(this.apiKey);

		SickRageApi.getInstance().init(preferences);
	}

	@Test
	public void getApiUrl() {
		assertThat(SickRageApi.getInstance().getApiUrl()).isEqualTo(this.url);
	}

	@Parameterized.Parameters(name = "{index} - {0}://{1}:{2}/{3}/{4}/")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{false, "", "", "", "", "http://127.0.0.1/"},
				{false, "127.0.0.1", "", "", "", "http://127.0.0.1/"},
				{false, "127.0.0.1", "8083", "", "", "http://127.0.0.1:8083/"},
				{false, "127.0.0.1", "8083", "api", "", "http://127.0.0.1:8083/api/"},
				{false, "127.0.0.1", "8083", "/api", "", "http://127.0.0.1:8083/api/"},
				{false, "127.0.0.1", "8083", "api/", "", "http://127.0.0.1:8083/api/"},
				{false, "127.0.0.1", "8083", "/api/", "", "http://127.0.0.1:8083/api/"},
				{false, "127.0.0.1", "8083", "/api1/api2/", "", "http://127.0.0.1:8083/api1/api2/"},
				{false, "127.0.0.1", "8083", "api", "apiKey", "http://127.0.0.1:8083/api/apiKey/"},
				{true, "", "", "", "", "http://127.0.0.1/"},
				{true, "127.0.0.1", "", "", "", "https://127.0.0.1/"},
				{true, "127.0.0.1", "8083", "", "", "https://127.0.0.1:8083/"},
				{true, "127.0.0.1", "8083", "api", "", "https://127.0.0.1:8083/api/"},
				{true, "127.0.0.1", "8083", "/api", "", "https://127.0.0.1:8083/api/"},
				{true, "127.0.0.1", "8083", "api/", "", "https://127.0.0.1:8083/api/"},
				{true, "127.0.0.1", "8083", "/api/", "", "https://127.0.0.1:8083/api/"},
				{true, "127.0.0.1", "8083", "/api1/api2/", "", "https://127.0.0.1:8083/api1/api2/"},
				{true, "127.0.0.1", "8083", "api", "apiKey", "https://127.0.0.1:8083/api/apiKey/"},
		});
	}
}
