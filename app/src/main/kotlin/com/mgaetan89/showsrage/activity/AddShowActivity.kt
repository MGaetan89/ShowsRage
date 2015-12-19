package com.mgaetan89.showsrage.activity

import android.content.Intent
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.fragment.AddShowFragment
import com.mgaetan89.showsrage.helper.GenericCallback
import com.mgaetan89.showsrage.model.GenericResponse
import retrofit.Callback
import retrofit.client.Response

class AddShowActivity : BaseActivity() {
    val addShowCallback: Callback<GenericResponse>
        get() = object : GenericCallback(this) {
            override fun success(genericResponse: GenericResponse?, response: Response?) {
                super.success(genericResponse, response)

                val intent = Intent(this@AddShowActivity, ShowsActivity::class.java)

                this@AddShowActivity.startActivity(intent)
            }
        }

    override fun displayHomeAsUp() = true

    override fun getFragment() = AddShowFragment()

    override fun getSelectedMenuId() = R.id.menu_shows

    override fun getTitleResourceId() = R.string.add_show
}
