package com.mgaetan89.showsrage.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.Spinner
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.network.SickRageApi

class ChangeQualityFragment : DialogFragment(), DialogInterface.OnClickListener {
	override fun onClick(dialog: DialogInterface?, which: Int) {
		val indexerId = this.arguments.getInt(Constants.Bundle.INDEXER_ID)
		val parentFragment = this.parentFragment

		if (this.dialog == null || indexerId <= 0 || parentFragment !is ShowOverviewFragment) {
			return
		}

		val allowedQualitySpinner = this.dialog.findViewById(R.id.allowed_quality) as Spinner?
		val allowedQuality = this.getAllowedQuality(allowedQualitySpinner)

		val preferredQualitySpinner = this.dialog.findViewById(R.id.preferred_quality) as Spinner?
		val preferredQuality = this.getPreferredQuality(preferredQualitySpinner)

		val callback = parentFragment.getSetShowQualityCallback()

		SickRageApi.instance.services?.setShowQuality(indexerId, allowedQuality, preferredQuality, callback)
	}

	override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
		val builder = AlertDialog.Builder(this.context)
		builder.setTitle(R.string.change_quality)
		builder.setView(R.layout.fragment_change_quality)
		builder.setNegativeButton(R.string.cancel, null)
		builder.setPositiveButton(R.string.change, this)

		return builder.show()
	}

	internal fun getAllowedQuality(allowedQualitySpinner: Spinner?): String? {
		var allowedQualityIndex = 0

		if (allowedQualitySpinner != null) {
			// Skip the "Ignore" first item
			allowedQualityIndex = allowedQualitySpinner.selectedItemPosition - 1
		}

		if (allowedQualityIndex >= 0) {
			val qualities = this.resources.getStringArray(R.array.allowed_qualities_keys)

			if (allowedQualityIndex < qualities.size) {
				return qualities[allowedQualityIndex]
			}
		}

		return null
	}

	internal fun getPreferredQuality(preferredQualitySpinner: Spinner?): String? {
		var preferredQualityIndex = 0

		if (preferredQualitySpinner != null) {
			// Skip the "Ignore" first item
			preferredQualityIndex = preferredQualitySpinner.selectedItemPosition - 1
		}

		if (preferredQualityIndex >= 0) {
			val qualities = this.resources.getStringArray(R.array.preferred_qualities_keys)

			if (preferredQualityIndex < qualities.size) {
				return qualities[preferredQualityIndex]
			}
		}

		return null
	}

	companion object {
		fun newInstance(indexerId: Int) = ChangeQualityFragment().apply {
			this.arguments = Bundle().apply {
				this.putInt(Constants.Bundle.INDEXER_ID, indexerId)
			}
		}
	}
}
