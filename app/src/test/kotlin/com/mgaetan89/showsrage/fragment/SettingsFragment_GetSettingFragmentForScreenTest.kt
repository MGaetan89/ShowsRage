package com.mgaetan89.showsrage.fragment

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class SettingsFragment_GetSettingFragmentForScreenTest(val screen: String?, val fragmentClass: Class<SettingsFragment>?) {
    @Test
    fun getSettingFragmentForPath() {
        if (this.fragmentClass == null) {
            assertThat(SettingsFragment.getSettingFragmentForScreen(this.screen)).isNull()
        } else {
            assertThat(SettingsFragment.getSettingFragmentForScreen(this.screen)).isExactlyInstanceOf(this.fragmentClass)
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
                    arrayOf<Any?>("screen_wrong", null),
                    arrayOf<Any?>("screen_about", SettingsAboutFragment::class.java),
                    arrayOf<Any?>("screen_about_licenses", SettingsAboutLicensesFragment::class.java),
                    arrayOf<Any?>("screen_about_shows_rage", SettingsAboutShowsRageFragment::class.java),
                    arrayOf<Any?>("screen_behavior", SettingsBehaviorFragment::class.java),
                    arrayOf<Any?>("screen_display", SettingsDisplayFragment::class.java),
                    arrayOf<Any?>("screen_experimental_features", SettingsExperimentalFeaturesFragment::class.java),
                    arrayOf<Any?>("screen_server", SettingsServerFragment::class.java),
                    arrayOf<Any?>("screen_server_api_key", SettingsServerApiKeyFragment::class.java)
            )
        }
    }
}
