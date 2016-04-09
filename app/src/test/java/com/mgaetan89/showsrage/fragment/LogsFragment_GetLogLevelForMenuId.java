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
public class LogsFragment_GetLogLevelForMenuId {
	@Parameterized.Parameter(1)
	public LogLevel logLevel;

	@Parameterized.Parameter(0)
	public int menuId;

	@Test
	public void getLogLevelForMenuId() {
		assertThat(LogsFragment.Companion.getLogLevelForMenuId(this.menuId)).isEqualTo(this.logLevel);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{0, null},
				{R.id.menu_debug, LogLevel.DEBUG},
				{R.id.menu_error, LogLevel.ERROR},
				{R.id.menu_info, LogLevel.INFO},
				{R.id.menu_warning, LogLevel.WARNING},
				{R.id.menu_change_quality, null},
		});
	}
}
