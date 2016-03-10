package com.mgaetan89.showsrage.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.*

class RootDirectoriesAdapter(context: Context, rootDirectories: Set<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ArrayList(rootDirectories)) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view = super.getView(position, convertView, parent)

        if (view is TextView) {
            view.text = this.getItem(position)
        }

        return view
    }
}
