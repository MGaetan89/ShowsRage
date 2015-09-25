package com.mgaetan89.showsrage.network;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class SickRageApi_GetCredentialsTest {
	@Parameterized.Parameter(3)
	public String credentials;

	@Parameterized.Parameter(2)
	public String password;

	@Parameterized.Parameter(0)
	public boolean useBasicAuthentication;

	@Parameterized.Parameter(1)
	public String username;

	@Test
	public void getCredentials() {
		assertThat(SickRageApi.getCredentials(this.useBasicAuthentication, this.username, this.password)).isEqualTo(this.credentials);
	}

	@Parameterized.Parameters(name = "{index}: {0} [{1}:{2}]")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{false, null, null, null},
				{false, null, "", null},
				{false, null, "password", null},
				{false, "", "password", null},
				{false, "username", "password", null},
				{false, "username", "", null},
				{false, "username", null, null},
				{true, null, null, null},
				{true, null, "", null},
				{true, null, "password", null},
				{true, "", "password", null},
				{true, "username", "password", "Basic dXNlcm5hbWU6cGFzc3dvcmQ="},
				{true, "username", "", null},
				{true, "username", null, null},
		});
	}
}
