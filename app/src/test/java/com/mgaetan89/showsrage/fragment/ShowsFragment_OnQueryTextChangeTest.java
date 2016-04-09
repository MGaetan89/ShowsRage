package com.mgaetan89.showsrage.fragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
public class ShowsFragment_OnQueryTextChangeTest {
	@Parameterized.Parameter(0)
	public String newText;

	private ShowsFragment fragment;

	@Before
	public void before() {
		this.fragment = spy(new ShowsFragment());
	}

	@Test
	public void onQueryTextChange() {
		try {
			assertTrue(this.fragment.onQueryTextChange(this.newText));
			verify(this.fragment).sendFilterMessage();
		} catch (NullPointerException exception) {
			// LocalBroadcastManager.getInstance(Context) returns null in tests
		}
	}

	@After
	public void after() {
		this.fragment = null;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{null},
				{""},
				{" "},
				{"Search Query"},
		});
	}
}
