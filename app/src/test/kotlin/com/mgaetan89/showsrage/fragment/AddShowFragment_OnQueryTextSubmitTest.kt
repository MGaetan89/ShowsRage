package com.mgaetan89.showsrage.fragment

import android.content.SharedPreferences
import com.mgaetan89.showsrage.network.SickRageApi
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class AddShowFragment_OnQueryTextSubmitTest(val query: String?, val valid: Boolean) {
	private lateinit var fragment: AddShowFragment

	@Before
	fun before() {
		val preferences = mock(SharedPreferences::class.java)
		`when`(preferences.getString(anyString(), anyString())).thenReturn("")

		SickRageApi.instance.init(preferences)

		this.fragment = AddShowFragment()
	}

	@Test
	fun onQueryTextSubmit() {
		assertThat(this.fragment.onQueryTextSubmit(this.query)).isEqualTo(this.valid)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data() = AddShowFragment_IsQueryValidTest.data()
	}
}
