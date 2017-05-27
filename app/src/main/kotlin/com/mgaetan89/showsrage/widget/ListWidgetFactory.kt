package com.mgaetan89.showsrage.widget

import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.mgaetan89.showsrage.R
import com.mgaetan89.showsrage.extension.getPreferences
import com.mgaetan89.showsrage.extension.useDarkTheme
import com.mgaetan89.showsrage.helper.Utils
import com.mgaetan89.showsrage.network.SickRageApi
import retrofit.RetrofitError

abstract class ListWidgetFactory<out T>(protected val context: Context) : RemoteViewsService.RemoteViewsFactory {
	internal var itemLayout = R.layout.widget_list_adapter_dark
		private set
	private var items = mutableListOf<T>()
	internal var loadingLayout = R.layout.widget_adapter_loading_dark
		private set

	init {
		SickRageApi.instance.init(this.context.getPreferences())

		Utils.initRealm(this.context)

		this.setLayoutFiles()
	}

	override fun getCount() = this.items.size

	fun getItem(position: Int) = this.items.getOrNull(position)

	override fun getItemId(position: Int) = position.toLong()

	override fun getLoadingView() = RemoteViews(this.context.packageName, this.loadingLayout)

	override fun getViewTypeCount() = 1

	override fun hasStableIds() = true

	override fun onCreate() = Unit

	override fun onDataSetChanged() {
		this.setLayoutFiles()

		this.items.clear()

		try {
			this.items.addAll(this.getItems())
		} catch (exception: RetrofitError) {
			Log.e("ListWidgetFactory", "A network error has occurred", exception)
		}
	}

	override fun onDestroy() = Unit

	@Throws(RetrofitError::class)
	protected abstract fun getItems(): List<T>

	internal fun setLayoutFiles() {
		if (this.context.getPreferences().useDarkTheme()) {
			this.itemLayout = R.layout.widget_list_adapter_dark
			this.loadingLayout = R.layout.widget_adapter_loading_dark
		} else {
			this.itemLayout = R.layout.widget_list_adapter_light
			this.loadingLayout = R.layout.widget_adapter_loading_light
		}
	}
}
