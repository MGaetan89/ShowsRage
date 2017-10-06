package com.mgaetan89.showsrage.extension

import java.util.Locale

fun String.humanize(): String {
	return this.split(' ', '-').joinToString(" ") {
		if (it.isEmpty()) {
			it
		} else {
			it.first().toUpperCase() + it.drop(1).toLowerCase()
		}
	}
}

fun String.toLocale(): Locale? {
	if (this.isEmpty()) {
		return null
	}

	val defaultLocale = Locale.getDefault()

	Locale.setDefault(Locale.ENGLISH)

	val locale = Locale.getAvailableLocales().filter { locale ->
		locale.displayLanguage.startsWith(this, true)
	}.firstOrNull { it != null }

	Locale.setDefault(defaultLocale)

	return locale
}
