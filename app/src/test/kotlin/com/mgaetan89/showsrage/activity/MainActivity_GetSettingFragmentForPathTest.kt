package com.mgaetan89.showsrage.activity

import com.mgaetan89.showsrage.fragment.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MainActivity_GetSettingFragmentForPathTest(val path: String?, val fragmentClass: Class<SettingsFragment>?) {
    @Test
    fun getSettingFragmentForPath() {
        if (this.fragmentClass == null) {
            assertThat(MainActivity.getSettingFragmentForPath(this.path)).isNull()
        } else {
            assertThat(MainActivity.getSettingFragmentForPath(this.path)).isExactlyInstanceOf(this.fragmentClass)
        }
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(null, null),
                    arrayOf<Any?>("", null),
                    arrayOf<Any?>(" ", null),
                    arrayOf<Any?>("/", SettingsFragment::class.java),
                    arrayOf<Any?>("/wrong_path", null),
                    arrayOf<Any?>("/about", SettingsAboutFragment::class.java),
                    arrayOf<Any?>("/about/licenses", SettingsAboutLicensesFragment::class.java),
                    arrayOf<Any?>("/about/showsrage", SettingsAboutShowsRageFragment::class.java),
                    arrayOf<Any?>("/about", SettingsAboutFragment::class.java),
                    arrayOf<Any?>("/behavior", SettingsBehaviorFragment::class.java),
                    arrayOf<Any?>("/display", SettingsDisplayFragment::class.java),
                    arrayOf<Any?>("/experimental_features", SettingsExperimentalFeaturesFragment::class.java),
                    arrayOf<Any?>("/server", SettingsServerFragment::class.java),
                    arrayOf<Any?>("/server/api_key", SettingsServerApiKeyFragment::class.java)
            )
        }
    }
}
