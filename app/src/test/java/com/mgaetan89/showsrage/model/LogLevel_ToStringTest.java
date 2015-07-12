package com.mgaetan89.showsrage.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class LogLevel_ToStringTest {
	@Parameterized.Parameter(0)
	public LogLevel logLevel;

	@Parameterized.Parameter(1)
	public String result;

	@Test
	public void toStringTest() {
		assertThat(this.logLevel.toString()).isEqualTo(this.result);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{LogLevel.DEBUG, "debug"},
				{LogLevel.ERROR, "error"},
				{LogLevel.INFO, "info"},
				{LogLevel.WARNING, "warning"},
		});
	}
}
