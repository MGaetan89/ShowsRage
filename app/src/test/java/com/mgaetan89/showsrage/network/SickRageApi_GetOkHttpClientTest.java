package com.mgaetan89.showsrage.network;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SickRageApi_GetOkHttpClientTest {
	@Parameterized.Parameter(1)
	public boolean useBasicAuthentication;

	@Parameterized.Parameter(0)
	public boolean useSelfSignedCertificate;

	@Before
	public void before() {
		if (this.useBasicAuthentication) {
			SickRageApi.getInstance().credentials = "dXNlcm5hbWU6cGFzc3dvcmQ=";
		} else {
			SickRageApi.getInstance().credentials = null;
		}
	}

	@Test
	public void getOkHttpClient() throws IOException {
		OkHttpClient client = SickRageApi.getInstance().getOkHttpClient(this.useSelfSignedCertificate);
		Response response = new Response.Builder()
				.code(200)
				.protocol(Protocol.HTTP_2)
				.request(
						new Request.Builder()
								.url("http://www.google.com/")
								.build()
				)
				.build();

		assertThat(client).isNotNull();
		assertThat(client.getAuthenticator()).isNotNull();

		if (this.useBasicAuthentication) {
			assertThat(client.getAuthenticator().authenticate(null, response)).isNotNull();
		} else {
			assertThat(client.getAuthenticator().authenticate(null, response)).isNull();
		}

		if (this.useSelfSignedCertificate) {
			assertThat(client.getHostnameVerifier()).isNotNull();
			assertThat(client.getHostnameVerifier().verify("", null)).isTrue();
			assertThat(client.getSslSocketFactory()).isNotNull();
		} else {
			assertThat(client.getHostnameVerifier()).isNull();
			assertThat(client.getSslSocketFactory()).isNull();
		}
	}

	@After
	public void after() {
		SickRageApi.getInstance().credentials = null;
	}

	@Parameterized.Parameters(name = "{index}: {0} -> {1}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{false, false},
				{false, true},
				{true, false},
				{true, true},
		});
	}
}
