package com.mgaetan89.showsrage.fragment

import com.mgaetan89.showsrage.Constants
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

@RunWith(Parameterized::class)
class SettingsFragment_GetPreferredLanguageTest(val locale: Locale, val language: String?, val preferredLocale: Locale) {
    @Before
    fun before() {
        Locale.setDefault(this.locale)
    }

    @Test
    fun getPreferredLanguage() {
        assertThat(SettingsFragment.getPreferredLocale(this.language)).isEqualTo(this.preferredLocale)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any?>> {
            return listOf(
                    arrayOf<Any?>(Locale.ENGLISH, null, Locale.ENGLISH),
                    arrayOf<Any?>(Locale.ENGLISH, "", Locale.ENGLISH),
                    arrayOf<Any?>(Locale.ENGLISH, "de", Locale.ENGLISH),
                    arrayOf<Any?>(Locale.ENGLISH, "fr", Locale.FRENCH),
                    arrayOf<Any?>(Locale.ENGLISH, "en", Locale.ENGLISH),
                    arrayOf<Any?>(Locale.FRENCH, null, Locale.FRENCH),
                    arrayOf<Any?>(Locale.FRENCH, "", Locale.FRENCH),
                    arrayOf<Any?>(Locale.FRENCH, "de", Locale.FRENCH),
                    arrayOf<Any?>(Locale.FRENCH, "fr", Locale.FRENCH),
                    arrayOf<Any?>(Locale.FRENCH, "en", Locale.ENGLISH),
                    arrayOf<Any?>(Locale.GERMAN, null, Constants.DEFAULT_LOCALE),
                    arrayOf<Any?>(Locale.GERMAN, "", Constants.DEFAULT_LOCALE),
                    arrayOf<Any?>(Locale.GERMAN, "de", Constants.DEFAULT_LOCALE),
                    arrayOf<Any?>(Locale.GERMAN, "fr", Locale.FRENCH),
                    arrayOf<Any?>(Locale.GERMAN, "en", Locale.ENGLISH)
            )
        }
    }
}
