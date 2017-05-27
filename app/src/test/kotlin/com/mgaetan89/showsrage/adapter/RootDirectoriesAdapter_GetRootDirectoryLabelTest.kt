package com.mgaetan89.showsrage.adapter

import com.mgaetan89.showsrage.model.RootDir
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RootDirectoriesAdapter_GetRootDirectoryLabelTest(val rootDirectory: RootDir?, val label: String) {
	@Test
	fun getRootDirectoryLabel() {
		assertThat(RootDirectoriesAdapter.getRootDirectoryLabel(this.rootDirectory)).isEqualTo(this.label)
	}

	companion object {
		@JvmStatic
		@Parameterized.Parameters
		fun data(): Collection<Array<Any?>> {
			return listOf(
					arrayOf<Any?>(null, ""),
					arrayOf<Any?>(getRootDir("/home/videos/Shows", 0), "/home/videos/Shows"),
					arrayOf<Any?>(getRootDir("/home/videos/Shows", 1), "* /home/videos/Shows"),
					arrayOf<Any?>(getRootDir("/home/videos/Shows", 2), "/home/videos/Shows")
			)
		}

		private fun getRootDir(location: String, defaultDir: Int): RootDir {
			return RootDir().apply {
				this.defaultDir = defaultDir
				this.location = location
			}
		}
	}
}
