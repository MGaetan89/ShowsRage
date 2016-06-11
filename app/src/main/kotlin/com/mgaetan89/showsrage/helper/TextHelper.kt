package com.mgaetan89.showsrage.helper

import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import java.util.*

fun hasText(text: String?) = !text.isNullOrEmpty() && !"N/A".equals(text, true)

fun setText(fragment: Fragment, textView: TextView, text: String?, label: Int, layout: View?) {
    if (hasText(text)) {
        if (layout == null) {
            textView.text = fragment.getString(label, text)
            textView.visibility = View.VISIBLE
        } else {
            layout.visibility = View.VISIBLE
            textView.text = text
        }
    } else {
        layout?.visibility = View.GONE
        textView.visibility = View.GONE
    }
}

fun String.toLocale(): Locale? {
    if (this.isNullOrEmpty()) {
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
