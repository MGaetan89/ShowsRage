package com.mgaetan89.showsrage.fragment

import android.app.SearchManager
import android.content.Intent
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(Parameterized::class)
class AddShowFragment_GetQueryFromIntentTest(val intent: Intent?, val query: String) {
	@Test
	fun getQueryFromIntent() {
		if (this.intent != null) {
			`when`(this.intent.getStringExtra(SearchManager.QUERY)).thenReturn(this.query)
		}

		assertThat(AddShowFragment.getQueryFromIntent(this.intent)).isEqualTo(this.query)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			return listOf(
					arrayOf<Any?>(null, ""),
					arrayOf<Any?>(getMockedIntent(Intent.ACTION_DIAL), ""),
					arrayOf<Any?>(getMockedIntent(Intent.ACTION_SEARCH), ""),
					arrayOf<Any?>(getMockedIntent(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, null as String?), ""),
					arrayOf<Any?>(getMockedIntent(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, ""), ""),
					arrayOf<Any?>(getMockedIntent(Intent.ACTION_SEARCH).putExtra(SearchManager.QUERY, "Some query"), "Some query")
			)
		}

		private fun getMockedIntent(action: String): Intent {
			val intent = mock(Intent::class.java)
			`when`(intent.putExtra(anyString(), anyString())).thenReturn(intent)
			`when`(intent.action).thenReturn(action)

			return intent
		}
	}
}
