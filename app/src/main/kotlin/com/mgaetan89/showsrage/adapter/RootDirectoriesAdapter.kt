package com.mgaetan89.showsrage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mgaetan89.showsrage.model.RootDir
import io.realm.RealmBaseAdapter
import io.realm.RealmResults

class RootDirectoriesAdapter(rootDirectories: RealmResults<RootDir>) : RealmBaseAdapter<RootDir>(rootDirectories) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(android.R.layout.simple_list_item_1, parent, false)

        this.setView(view, this.getItem(position))

        return view
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view = convertView ?: LayoutInflater.from(parent?.context).inflate(android.R.layout.simple_list_item_1, parent, false)

        this.setView(view, this.getItem(position))

        return view
    }

    private fun setView(view: View?, rootDirectory: RootDir?) {
        if (view is TextView) {
            view.text = getRootDirectoryLabel(rootDirectory)
        }
    }

    companion object {
        internal fun getRootDirectoryLabel(rootDir: RootDir?): String {
            val location = rootDir?.location ?: ""
            val prefix = if (rootDir?.defaultDir == 1) "* " else ""

            return prefix + location
        }
    }
}
