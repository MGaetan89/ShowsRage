package com.mgaetan89.showsrage.fragment

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.getLogsGroup
import com.mgaetan89.showsrage.helper.humanize
import io.realm.Realm

class LogsFilterFragment : DialogFragment(), DialogInterface.OnClickListener, DialogInterface.OnMultiChoiceClickListener {
    private val items: Array<String> by lazy {
        val realm = Realm.getDefaultInstance()
        val logsGroup = realm.getLogsGroup()
        realm.close()

        logsGroup.toTypedArray()
    }
    private val itemsFormatted = Array(this.items.size) { "" }
    private val selectedIndices = mutableSetOf<Int>()

    init {
        this.items.forEachIndexed { i, item ->
            this.itemsFormatted[i] = item.humanize()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val selectedItems = getSelectedItems(this.items, this.selectedIndices)
        val data = Intent()
        data.putExtra(Constants.Bundle.LOGS_GROUPS, selectedItems)

        this.targetFragment.onActivityResult(this.targetRequestCode, Activity.RESULT_OK, data)

        dialog?.dismiss()
    }

    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
        if (isChecked) {
            this.selectedIndices.add(which)
        } else {
            this.selectedIndices.remove(which)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val groups = this.arguments.getStringArray(Constants.Bundle.LOGS_GROUPS)

        setSelectedIndices(this.selectedIndices, this.items, groups)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val checked = getCheckedStates(this.items.size, this.selectedIndices)
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(R.string.filter)
        builder.setMultiChoiceItems(this.itemsFormatted, checked, this)
        builder.setNegativeButton(R.string.cancel, null)
        builder.setPositiveButton(R.string.filter, this)

        return builder.show()
    }

    companion object {
        internal fun getCheckedStates(size: Int, selectedIndices: Set<Int>): BooleanArray {
            return arrayOfNulls<Boolean>(size).mapIndexed { i, item -> selectedIndices.contains(i) }.toBooleanArray()
        }

        internal fun getSelectedItems(items: Array<String>, selectedIndices: Set<Int>): Array<String> {
            return items.filterIndexed { i, item -> selectedIndices.contains(i) }.toTypedArray()
        }

        internal fun setSelectedIndices(selectedIndices: MutableSet<Int>, items: Array<String>, groups: Array<String>?) {
            selectedIndices.clear()

            if (groups == null || groups.isEmpty()) {
                selectedIndices.addAll(items.indices)
            } else {
                groups.forEach {
                    val index = items.indexOf(it)

                    if (index >= 0) {
                        selectedIndices.add(index)
                    }
                }
            }
        }
    }
}
