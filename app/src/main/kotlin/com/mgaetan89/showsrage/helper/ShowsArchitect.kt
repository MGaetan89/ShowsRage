package com.mgaetan89.showsrage.helper

import android.widget.TextView
import io.kolumbus.Architect

class ShowsArchitect : Architect() {
    override fun displayInt(textView: TextView, value: Int) {
        val show = RealmManager.getShow(value)

        if (show != null) {
            // TODO
            /*
            textView.setOnClickListener {
                TableActivity.start(textView.context, Show::class.java, arrayOf(show))
            }
            */

            super.displayString(textView, "[$value] ${show.showName}")
        } else {
            super.displayInt(textView, value)
        }
    }
}
