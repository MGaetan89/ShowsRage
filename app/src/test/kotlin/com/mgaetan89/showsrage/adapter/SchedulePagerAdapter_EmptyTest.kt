package com.mgaetan89.showsrage.adapter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class SchedulePagerAdapter_EmptyTest {
	private lateinit var adapter: SchedulePagerAdapter

	@Before
	fun before() {
		this.adapter = SchedulePagerAdapter(null, emptyList<String>(), emptyList<String>())
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(0)
	}
}
