package com.mgaetan89.showsrage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mgaetan89.showsrage.model.ComingEpisodes;
import com.mgaetan89.showsrage.network.SickRageApi;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ComingEpisodesFragment extends Fragment implements Callback<ComingEpisodes> {
	public ComingEpisodesFragment() {
	}

	@Override
	public void failure(RetrofitError error) {
		error.printStackTrace();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SickRageApi.getInstance().getServices().getComingEpisodes(this);
	}

	@Override
	public void success(ComingEpisodes comingEpisodes, Response response) {
	}
}
