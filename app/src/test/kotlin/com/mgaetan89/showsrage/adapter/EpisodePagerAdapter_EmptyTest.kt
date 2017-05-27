package com.mgaetan89.showsrage.adapter

import android.support.v4.app.Fragment
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class EpisodePagerAdapter_EmptyTest {
	private lateinit var adapter: EpisodePagerAdapter

	@Before
	fun before() {
		this.adapter = EpisodePagerAdapter(null, mock(Fragment::class.java), emptyList<Int>())
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(0)
	}
}
