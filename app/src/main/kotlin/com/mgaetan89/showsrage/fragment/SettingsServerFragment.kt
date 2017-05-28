package com.mgaetan89.showsrage.fragment

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

open class SettingsServerFragment : SettingsFragment(), Callback<GenericResponse> {
	private var alertDialog: AlertDialog? = null

	private var canceled = false

	init {
		this.setHasOptionsMenu(true)
	}

	override fun failure(error: RetrofitError?) {
		this.showTestResult(false)
	}

	override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
		inflater?.inflate(R.menu.settings_server, menu)
	}

	override fun onDestroy() {
		this.canceled = true

		this.dismissDialog()

		super.onDestroy()
	}

	override fun onOptionsItemSelected(item: MenuItem?): Boolean {
		when (item?.itemId) {
			R.id.menu_help -> {
				this.displayConfigurationHelp()

				return true
			}

			R.id.menu_test -> {
				this.testConnection()

				return true
			}
		}

		return super.onOptionsItemSelected(item)
	}

	override fun success(genericResponse: GenericResponse?, response: Response?) {
		this.showTestResult(true)
	}

	override fun getTitleResourceId() = R.string.server

	override fun getXmlResourceFile() = R.xml.settings_server

	private fun dismissDialog() {
		this.alertDialog?.let {
			if (it.isShowing) {
				it.dismiss()
			}
		}

		this.alertDialog = null
	}

	private fun displayConfigurationHelp() {
		val intent = Intent(Intent.ACTION_VIEW)
		intent.data = Uri.parse("https://MGaetan89.github.io/ShowsRage/help.html#how-to-configure-showsrage")

		this.startActivity(intent)
	}

	private fun showTestResult(successful: Boolean) {
		if (this.canceled) {
			return
		}

		this.dismissDialog()

		val context = this.activity ?: return
		val url = SickRageApi.instance.getApiUrl()

		this.alertDialog = AlertDialog.Builder(context)
				.setCancelable(true)
				.setMessage(if (successful) this.getString(R.string.connection_successful) else this.getString(R.string.connection_failed, url))
				.setPositiveButton(android.R.string.ok, null)
				.show()
	}

	private fun testConnection() {
		val activity = this.activity ?: return

		this.canceled = false

		this.dismissDialog()

		SickRageApi.instance.init(activity.getPreferences())

		this.alertDialog = AlertDialog.Builder(activity)
				.setCancelable(true)
				.setTitle(R.string.testing_server_settings)
				.setMessage(this.getString(R.string.connecting_to, SickRageApi.instance.getApiUrl()))
				.setNegativeButton(R.string.cancel, { dialog, _ ->
					SettingsServerFragment@ this.canceled = true

					dialog.dismiss()
				})
				.show()

		SickRageApi.instance.services?.ping(this)
	}

	companion object {
		fun newInstance() = SettingsServerFragment()
	}
}
