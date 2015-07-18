package com.mgaetan89.showsrage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.Constants;
import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter;
import com.mgaetan89.showsrage.fragment.AddShowFragment;
import com.mgaetan89.showsrage.fragment.AddShowOptionsFragment;
import com.mgaetan89.showsrage.helper.GenericCallback;
import com.mgaetan89.showsrage.model.GenericResponse;

import retrofit.Callback;
import retrofit.client.Response;

public class AddShowActivity extends BaseActivity implements SearchResultsAdapter.OnSearchResultSelectedListener {
	@NonNull
	public Callback<GenericResponse> getAddShowCallback() {
		return new GenericCallback(this) {
			@Override
			public void success(GenericResponse genericResponse, Response response) {
				super.success(genericResponse, response);

				Intent intent = new Intent(AddShowActivity.this, ShowsActivity.class);

				AddShowActivity.this.startActivity(intent);
			}
		};
	}

	@Override
	public void onSearchResultSelected(int indexerId) {
		Bundle arguments = new Bundle();
		arguments.putInt(Constants.Bundle.INDEXER_ID, indexerId);

		AddShowOptionsFragment fragment = new AddShowOptionsFragment();
		fragment.setArguments(arguments);
		fragment.show(this.getSupportFragmentManager(), "add_show");
	}

	@Override
	protected boolean displayHomeAsUp() {
		return true;
	}

	@Override
	protected Fragment getFragment() {
		return new AddShowFragment();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.add_show;
	}
}
