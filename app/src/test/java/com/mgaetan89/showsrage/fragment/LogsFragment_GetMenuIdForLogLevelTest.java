package com.mgaetan89.showsrage.fragment;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.LogLevel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class LogsFragment_GetMenuIdForLogLevelTest {
	@Parameterized.Parameter(0)
	public LogLevel logLevel;

	@Parameterized.Parameter(1)
	public int menuId;

	@Test
	public void getMenuIdForLogLevel() {
		assertThat(LogsFragment.getMenuIdForLogLevel(this.logLevel)).isEqualTo(this.menuId);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, 0},
				{LogLevel.DEBUG, R.id.menu_debug},
				{LogLevel.ERROR, R.id.menu_error},
				{LogLevel.INFO, R.id.menu_info},
				{LogLevel.WARNING, R.id.menu_warning},
		});
	}
}
