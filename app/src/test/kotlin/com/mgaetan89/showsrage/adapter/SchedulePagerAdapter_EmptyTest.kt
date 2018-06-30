package com.mgaetan89.showsrage.adapter

import android.support.v4.app.FragmentManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SchedulePagerAdapter_EmptyTest {
	private lateinit var adapter: SchedulePagerAdapter

	@Before
	fun before() {
		this.adapter = SchedulePagerAdapter(mock(FragmentManager::class.java), emptyList(), emptyList())
	}

	@Test
	fun getCount() {
		assertThat(this.adapter.count).isEqualTo(0)
	}
}
