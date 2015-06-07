package com.mgaetan89.showsrage.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.mgaetan89.showsrage.R;
import com.mgaetan89.showsrage.adapter.SearchResultsAdapter;
import com.mgaetan89.showsrage.fragment.AddShowFragment;
import com.mgaetan89.showsrage.model.GenericResponse;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddShowActivity extends BaseActivity implements Callback<GenericResponse>, SearchResultsAdapter.OnSearchResultSelectedListener {
	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onSearchResultSelected(int indexerId) {
		SickRageApi.getInstance().getServices().addNewShow(indexerId, this);
	}

	@Override
	public void success(GenericResponse genericResponse, Response response) {
		Toast.makeText(this, genericResponse.getMessage(), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected int getSelectedMenuId() {
		return R.id.menu_shows;
	}

	@Override
	protected int getTitleResourceId() {
		return R.string.add_show;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.getSupportFragmentManager().beginTransaction()
					.replace(R.id.content, new AddShowFragment())
					.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.displayHomeAsUp(true);
	}
}
