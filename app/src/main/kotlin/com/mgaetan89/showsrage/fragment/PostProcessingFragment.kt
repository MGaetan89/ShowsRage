package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.widget.Checkable
import android.widget.Spinner
import android.widget.Switch
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.network.SickRageApi

open class PostProcessingFragment : DialogFragment(), DialogInterface.OnClickListener {
    private var forceProcessing: Switch? = null
    private var processingMethod: Spinner? = null
    private var replaceFiles: Switch? = null

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val force = checkableToInteger(this.forceProcessing)
        val method = this.getProcessingMethod(this.processingMethod)
        val replace = checkableToInteger(this.replaceFiles)

        SickRageApi.instance.services?.postProcess(replace, force, method, GenericCallback(this.activity))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_post_processing, null)

        if (view != null) {
            this.forceProcessing = view.findViewById(R.id.force_processing) as Switch?
            this.processingMethod = view.findViewById(R.id.processing_method) as Spinner?
            this.replaceFiles = view.findViewById(R.id.replace_files) as Switch?
        }

        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(R.string.post_processing)
        builder.setView(view)
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.setPositiveButton(R.string.process, this)

        return builder.create()
    }

    override fun onDestroyView() {
        this.forceProcessing = null
        this.processingMethod = null
        this.replaceFiles = null

        super.onDestroyView()
    }

    fun getProcessingMethod(spinner: Spinner?): String? {
        var processingMethodIndex = 0

        if (spinner != null) {
            processingMethodIndex = spinner.selectedItemPosition
        }

        if (processingMethodIndex > 0) {
            val processingMethods = this.resources.getStringArray(R.array.processing_methods_values)

            if (processingMethodIndex < processingMethods.size) {
                return processingMethods[processingMethodIndex]
            }
        }

        return null
    }

    companion object {
        fun checkableToInteger(checkable: Checkable?): Int {
            return if (checkable?.isChecked ?: false) 1 else 0
        }
    }
}
