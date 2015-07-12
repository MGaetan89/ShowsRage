package com.mgaetan89.showsrage.model;

import com.mgaetan89.showsrage.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class LogEntryTest {
	@Parameterized.Parameter(1)
	public String dateTime;

	@Parameterized.Parameter(3)
	public int errorColor;

	@Parameterized.Parameter(2)
	public String errorType;

	@Parameterized.Parameter(0)
	public String log;

	@Parameterized.Parameter(4)
	public String message;

	private LogEntry logEntry;

	@Before
	public void before() {
		this.logEntry = new LogEntry(this.log);
	}

	@Test
	public void getDateTime() {
		assertThat(this.logEntry.getDateTime()).isEqualTo(this.dateTime);
	}

	@Test
	public void getErrorColor() {
		assertThat(this.logEntry.getErrorColor()).isEqualTo(this.errorColor);
	}

	@Test
	public void getErrorType() {
		assertThat(this.logEntry.getErrorType()).isEqualTo(this.errorType);
	}

	@Test
	public void getMessage() {
		assertThat(this.logEntry.getMessage()).isEqualTo(this.message);
	}

	@After
	public void after() {
		this.logEntry = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(
				getInvalidLogEntry(null),
				getInvalidLogEntry(""),
				getInvalidLogEntry("2015-06-01 DEBUG Some DEBUG message"),
				getValidLogEntry("17:28:45", "DEBUG", R.color.green, "Some DEBUG message"),
				getValidLogEntry("2015-06-02 17:28:46", "DEBUG", R.color.green, "Some DEBUG message"),
				getInvalidLogEntry("2015-06-03 ERROR Some ERROR message"),
				getValidLogEntry("17:28:47", "ERROR", R.color.red, "Some ERROR message"),
				getValidLogEntry("2015-06-04 17:28:48", "ERROR", R.color.red, "Some ERROR message"),
				getInvalidLogEntry("2015-06-05 INFO Some INFO message"),
				getValidLogEntry("17:28:49", "INFO", R.color.blue, "Some INFO message"),
				getValidLogEntry("2015-06-06 17:28:50", "INFO", R.color.blue, "Some INFO message"),
				getInvalidLogEntry("2015-06-07 WARNING Some WARNING message"),
				getValidLogEntry("17:28:51", "WARNING", R.color.orange, "Some WARNING message"),
				getValidLogEntry("2015-06-08 17:28:52", "WARNING", R.color.orange, "Some WARNING message")
		);
	}

	private static Object[] getInvalidLogEntry(String log) {
		return new Object[]{log, "", "", android.R.color.black, ""};
	}

	private static Object[] getValidLogEntry(String dateTime, String errorType, int errorColor, String message) {
		return new Object[]{
				String.format("%s %s %s", dateTime, errorType, message),
				dateTime,
				errorType,
				errorColor,
				message
		};
	}
}
