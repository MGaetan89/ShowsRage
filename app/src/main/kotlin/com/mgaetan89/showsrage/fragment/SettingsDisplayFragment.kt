package com.mgaetan89.showsrage.fragment

import android.appwidget.AppWidgetManager
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.Fields
import com.mgaetan89.showsrage.extension.changeLocale
import com.mgaetan89.showsrage.extension.getLocale
import com.mgaetan89.showsrage.extension.updateAllWidgets
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.widget.HistoryWidgetProvider
import com.mgaetan89.showsrage.widget.ScheduleWidgetProvider

class SettingsDisplayFragment : SettingsFragment() {
	override fun getTitleResourceId() = R.string.display

	override fun getXmlResourceFile() = R.xml.settings_display

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		super.onSharedPreferenceChanged(sharedPreferences, key)

		when (key) {
			Fields.DISPLAY_LANGUAGE.field -> this.changeLanguage(sharedPreferences)
			Fields.THEME.field -> this.changeTheme(sharedPreferences)
		}
	}

	private fun changeLanguage(sharedPreferences: SharedPreferences?) {
		val newLocale = sharedPreferences.getLocale()

		this.resources.changeLocale(newLocale)

		this.activity?.recreate()

		this.updateWidgets()
	}

	private fun changeTheme(sharedPreferences: SharedPreferences?) {
		if (sharedPreferences.useDarkTheme()) {
			(this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
		} else {
			(this.activity as AppCompatActivity).delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
		}

		this.updateWidgets()
	}

	private fun updateWidgets() {
		val context = this.context ?: return

		AppWidgetManager.getInstance(this.context).let {
			it.updateAllWidgets(context, HistoryWidgetProvider::class.java)
			it.updateAllWidgets(context, ScheduleWidgetProvider::class.java)
		}
	}

	companion object {
		fun newInstance() = SettingsDisplayFragment()
	}
}
