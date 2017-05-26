package com.mgaetan89.showsrage.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.mgaetan89.showsrage.Constants
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.model.GenericResponse
import com.mgaetan89.showsrage.model.UpdateResponse
import com.mgaetan89.showsrage.network.SickRageApi
import com.mgaetan89.showsrage.service.UpdateService
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class UpdateActivity : AppCompatActivity(), Callback<GenericResponse>, DialogInterface.OnClickListener {
	override fun failure(error: RetrofitError?) {
		error?.printStackTrace()
	}

	override fun onBackPressed() {
		super.onBackPressed()

		this.finish()
	}

	override fun onClick(dialog: DialogInterface?, which: Int) {
		when (which) {
			DialogInterface.BUTTON_POSITIVE -> this.updateSickRage()
			DialogInterface.BUTTON_NEGATIVE -> this.finish()
		}
	}

	override fun success(genericResponse: GenericResponse?, response: Response?) {
		val message = genericResponse?.message.orEmpty()
		if (message.isNotBlank()) {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
		}

		this.finish()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		SickRageApi.instance.init(this.getPreferences())

		val update = this.intent.extras.getParcelable<UpdateResponse>(Constants.Bundle.UPDATE_MODEL)
		val builder = AlertDialog.Builder(this)
				.setTitle(R.string.update_sickrage)

		if (update != null) {
			builder.setMessage(this.getString(R.string.update_available_detailed, update.currentVersion?.version, update.latestVersion?.version, update.commitsOffset))
		}

		builder.setPositiveButton(R.string.update, this)
				.setNegativeButton(R.string.ignore, this)
				.show()
	}

	private fun updateSickRage() {
		SickRageApi.instance.services?.updateSickRage(this)

		this.startService(Intent(this, UpdateService::class.java))

		Toast.makeText(this, R.string.updating_sickrage, Toast.LENGTH_SHORT).show()

		this.finish()
	}
}
