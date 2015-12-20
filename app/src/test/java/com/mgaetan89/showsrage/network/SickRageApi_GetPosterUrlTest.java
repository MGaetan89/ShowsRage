package com.mgaetan89.showsrage.network;

import android.content.SharedPreferences;

import com.mgaetan89.showsrage.model.Indexer;

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
public class SickRageApi_GetPosterUrlTest {
	@Parameterized.Parameter(1)
	public String address;

	@Parameterized.Parameter(4)
	public String apiKey;

	@Parameterized.Parameter(6)
	public Indexer indexer;

	@Parameterized.Parameter(5)
	public int indexerId;

	@Parameterized.Parameter(3)
	public String path;

	@Parameterized.Parameter(2)
	public String port;

	@Parameterized.Parameter(7)
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

		SickRageApi.Companion.getInstance().init(preferences);
	}

	@Test
	public void getPosterUrl() {
		assertThat(SickRageApi.Companion.getInstance().getPosterUrl(this.indexerId, this.indexer)).isEqualTo(this.url);
	}

	@Parameterized.Parameters(name = "[{6}] {index} - {0}://{1}:{2}/{3}/{4}/")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				// TVDB
				{false, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"},
				{false, "127.0.0.1", "", "", "", 123, null, "http://127.0.0.1/?cmd=show.getposter"},
				{false, "127.0.0.1", "8083", "", "", 0, Indexer.TVDB, "http://127.0.0.1:8083/?cmd=show.getposter&tvdbid=0"},
				{false, "127.0.0.1", "8083", "api", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{false, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{false, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{false, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{false, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVDB, "http://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvdbid=123"},
				{false, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVDB, "http://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123"},
				{true, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"},
				{true, "127.0.0.1", "", "", "", 123, null, "https://127.0.0.1/?cmd=show.getposter"},
				{true, "127.0.0.1", "8083", "", "", 0, Indexer.TVDB, "https://127.0.0.1:8083/?cmd=show.getposter&tvdbid=0"},
				{true, "127.0.0.1", "8083", "api", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{true, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{true, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{true, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvdbid=123"},
				{true, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVDB, "https://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvdbid=123"},
				{true, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVDB, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvdbid=123"},
				// TVRage
				{false, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"},
				{false, "127.0.0.1", "", "", "", 123, null, "http://127.0.0.1/?cmd=show.getposter"},
				{false, "127.0.0.1", "8083", "", "", 0, Indexer.TVRAGE, "http://127.0.0.1:8083/?cmd=show.getposter&tvrageid=0"},
				{false, "127.0.0.1", "8083", "api", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{false, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{false, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{false, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{false, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvrageid=123"},
				{false, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVRAGE, "http://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvrageid=123"},
				{true, "", "", "", "", 0, null, "http://127.0.0.1/?cmd=show.getposter"},
				{true, "127.0.0.1", "", "", "", 123, null, "https://127.0.0.1/?cmd=show.getposter"},
				{true, "127.0.0.1", "8083", "", "", 0, Indexer.TVRAGE, "https://127.0.0.1:8083/?cmd=show.getposter&tvrageid=0"},
				{true, "127.0.0.1", "8083", "api", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{true, "127.0.0.1", "8083", "/api", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{true, "127.0.0.1", "8083", "api/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{true, "127.0.0.1", "8083", "/api/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/?cmd=show.getposter&tvrageid=123"},
				{true, "127.0.0.1", "8083", "/api1/api2/", "", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api1/api2/?cmd=show.getposter&tvrageid=123"},
				{true, "127.0.0.1", "8083", "api", "apiKey", 123, Indexer.TVRAGE, "https://127.0.0.1:8083/api/apiKey/?cmd=show.getposter&tvrageid=123"},
		});
	}
}
