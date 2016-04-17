package com.mgaetan89.showsrage.presenter;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.model.LogEntry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class LogPresenterTest {
	@Parameterized.Parameter(1)
	public CharSequence dateTime;

	@Parameterized.Parameter(2)
	public int errorColor;

	@Parameterized.Parameter(3)
	public String errorType;

	@Parameterized.Parameter(0)
	public LogEntry logEntry;

	@Parameterized.Parameter(4)
	public String message;

	private LogPresenter presenter;

	@Before
	public void before() {
		this.presenter = new LogPresenter(this.logEntry);
	}

	@Test
	public void getDateTime() {
		assertThat(this.presenter.getDateTime()).isEqualTo(this.dateTime);
	}

	@Test
	public void getErrorColor() {
		assertThat(this.presenter.getErrorColor()).isEqualTo(this.errorColor);
	}

	@Test
	public void getErrorType() {
		assertThat(this.presenter.getErrorType()).isEqualTo(this.errorType);
	}

	@Test
	public void getMessage() {
		assertThat(this.presenter.getMessage()).isEqualTo(this.message);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null, "", android.R.color.white, "", ""},
				{new LogEntry("2015-12-31 01:02:03 DEBUG Some debug message"), null, R.color.green, "DEBUG", "Some debug message"},
				{new LogEntry("2015-12-31 01:02:03 ERROR Some error message"), null, R.color.red, "ERROR", "Some error message"},
				{new LogEntry("2015-12-31 01:02:03 INFO Some info message"), null, R.color.blue, "INFO", "Some info message"},
				{new LogEntry("2015-12-31 01:02:03 WARNING Some warning message"), null, R.color.orange, "WARNING", "Some warning message"},
				{new LogEntry("2015-12-31 01:02:03 DEBUG DAILYSEARCHER :: Some debug message"), null, R.color.green, "DEBUG", "DAILYSEARCHER :: Some debug message"},
				{new LogEntry("2015-12-31 01:02:03 ERROR DAILYSEARCHER :: Some error message"), null, R.color.red, "ERROR", "DAILYSEARCHER :: Some error message"},
				{new LogEntry("2015-12-31 01:02:03 INFO DAILYSEARCHER :: Some info message"), null, R.color.blue, "INFO", "DAILYSEARCHER :: Some info message"},
				{new LogEntry("2015-12-31 01:02:03 WARNING DAILYSEARCHER :: Some warning message"), null, R.color.orange, "WARNING", "DAILYSEARCHER :: Some warning message"},
		});
	}
}
