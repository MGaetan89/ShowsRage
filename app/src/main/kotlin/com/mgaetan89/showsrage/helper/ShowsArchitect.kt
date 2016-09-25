package com.mgaetan89.showsrage.helper

import android.widget.TextView
import com.mgaetan89.showsrage.extension.getShow
import io.kolumbus.Architect
import io.realm.Realm

class ShowsArchitect : Architect() {
    override fun displayInt(textView: TextView, value: Int) {
        val realm = Realm.getDefaultInstance()
        val show = realm.getShow(value)

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

        realm.close()
    }
}
