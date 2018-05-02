package com.mgaetan89.showsrage.fragment

import android.support.v7.app.AlertDialog
import android.support.v7.preference.Preference
import com.mgaetan89.showsrage.R
import io.realm.Realm

class SettingsBehaviorFragment : SettingsFragment() {
	override fun getTitleResourceId() = R.string.behavior

	override fun getXmlResourceFile() = R.xml.settings_behavior

	override fun onPreferenceTreeClick(preference: Preference?): Boolean {
		if ("behavior_clear_local" == preference?.key) {
			this.confirmClearLocalData()

			return true
		}

		return super.onPreferenceTreeClick(preference)
	}

	private fun confirmClearLocalData() {
		val context = this.context ?: return

		AlertDialog.Builder(context)
				.setMessage(R.string.clear_local_data_confirm)
				.setPositiveButton(R.string.clear, { _, _ ->
					Realm.getDefaultInstance().let {
						it.executeTransaction(Realm::deleteAll)
						it.close()
					}
				})
				.setNegativeButton(android.R.string.cancel, null)
				.show()
	}

	companion object {
		fun newInstance() = SettingsBehaviorFragment()
	}
}
